package com.iiit.adb.emp.db.parser.WhereTree;

import java.util.ArrayList;
import java.util.Iterator;

import com.iiit.adb.emp.db.parser.globaldefinition.JoinExpression;
import com.iiit.adb.emp.db.parser.globaldefinition.SimpleExpression;
import com.iiit.adb.emp.dblayer.syscatalogue.Tbl;

public class WhereTree {
	private WhereNode root = null;
	ArrayList<Tbl> tbl = null;
	String cstr = new String();
	ArrayList<JoinExpression> jelist = new ArrayList<JoinExpression>();
	ArrayList<SimpleExpression> selist =  new ArrayList<SimpleExpression>();
	
	public WhereTree(){
	}
	public WhereNode getRoot(){
		return root;
	}
	public void setRoot(WhereNode r){
		root = r;
	}
	public void displayTree(){
		if (root == null) return;
		display(root);
	}
	private void display(WhereNode node){
		if(node.IsLeaf()){
			node.display();
		}
		else{
			display(node.getLeftChild());
			node.display();
			display(node.getRightChild());
		}
	}
	
	public void collectJoins(WhereNode node){
		if(node instanceof OpNode){
			OpNode onode = (OpNode)node;
			AttrNode leftattr = null;
			AttrNode rightattr = null;
			
			if ((onode.getLeftChild() instanceof AttrNode) && (onode.getRightChild() instanceof AttrNode) ){
				// it is a join expression
				JoinExpression je = new JoinExpression();
				 leftattr =  (AttrNode)onode.getLeftChild();
				 rightattr = (AttrNode)onode.getRightChild();
				
				je.leftColumn = leftattr.getAttrName();
				je.leftTableName = leftattr.getTableName();
				
				je.op = onode.getOp();
				
				je.rigthColumn = rightattr.getAttrName();
				je.rightTableName = rightattr.getTableName();
				jelist.add(je);
			} else {
				// this is simpleexpression for now. we are not handling having, group by etc..
				SimpleExpression se = new SimpleExpression();
				if (onode.getLeftChild() instanceof AttrNode) {
					leftattr =  (AttrNode)onode.getLeftChild();
					se.columnName = leftattr.getAttrName();
					se.tableName = leftattr.getTableName();
					se.op = onode.getOp();
					ValueNode vnode = (ValueNode)onode.getRightChild();
					se.setValType(vnode.getValueType());
					se.value = vnode.getValue();
				} else {
					rightattr =  (AttrNode)onode.getRightChild();
					se.columnName = rightattr.getAttrName();
					se.tableName = rightattr.getTableName();
					se.op = onode.getOp();
					ValueNode vnode = (ValueNode)onode.getLeftChild();
					se.setValType(vnode.getValueType());
					se.value = vnode.getValue();
				}
				
			}
		}
		else{
			collectJoins(node.getLeftChild());
			//node.display();
			collectJoins(node.getRightChild());
		}
	}
	
	public void validateWhere(ArrayList<Tbl> tbl, String cstr) throws Exception{
		// This is where the checking into the db layer should be done
		this.tbl = tbl;
		this.cstr = cstr;
		if (root == null) return;
		validate(root);
		
		
	}
	
	private void validate(WhereNode node) throws Exception{
		if ((node instanceof ValueNode) || (node instanceof AttrNode)){
			return;
		}
		if((node instanceof AndNode) || (node instanceof OrNode))
		{
			validate(node.getLeftChild());
			//node.display();
			validate(node.getRightChild());
			return;
		}
		// what should we do here. may be we should do it on "=", <=,>=,<,>
		if (node instanceof OpNode){
			OpNode onode = (OpNode)node;
			String op = onode.getOp();
			String [] S = null;//should be initialized
			String type=new String(), type1= new String();
			// get left and right child and validate
			// left and right child could be table.column = table.column
			if (node.getLeftChild() instanceof AttrNode && node.getRightChild() instanceof ValueNode){
				// collect attribute
				AttrNode anode = (AttrNode)node.getLeftChild();
				String colname = anode.getAttrName();
				String tablename = anode.getTableName();
				//-------
				ValueNode bnode = (ValueNode)node.getRightChild();
				String value = bnode.getValue();
				type1 = bnode.getValueType();
				if(type1.matches("string"))
					type1="varchar";
				else if(type1.matches("long"))
					type1="int";
				boolean flag = false;
				if(tablename==null ) {
					for (Iterator j = tbl.iterator();j.hasNext();){
						Tbl t1 = (Tbl)j.next();
						S = t1.getColNames().split(":");
						int i;
						for(i=0;i<S.length;i++)
							if(S[i].matches(colname))
							{
								flag = true;
								break;
							}
						if(flag==true)
						{	
							type= t1.getColTypes().split(":")[i];
							break;
						}
					}
				}
				else {
					for (Iterator j = tbl.iterator();j.hasNext();){
						Tbl t1 = (Tbl)j.next();
						if (tablename.equals(t1.getTblName()) && t1.getColNames().contains(colname) ) {
							 //we have to use split number and retrieve corresponding type n check
							int i;
							S = t1.getColNames().split(":");
							for(i=0;i<S.length;i++) {
								if(colname.matches(S[i]))
									break;
							}
							type= t1.getColTypes().split(":")[i];
							flag = true;
							break;
						}
					}
				}
				if(flag==false)
					throw (new Exception("Invalid where clause"));
				// else throw exception
			}//***********
			else if (node.getRightChild() instanceof AttrNode && node.getLeftChild() instanceof ValueNode){
				// collect attribute
				AttrNode anode = (AttrNode)node.getRightChild();
				String colname = anode.getAttrName();
				String tablename = anode.getTableName();
				//-------
				ValueNode bnode = (ValueNode)node.getLeftChild();
				String value = bnode.getValue();
				type1 = bnode.getValueType();
				if(type1.matches("string"))
					type1="varchar";
				else if(type1.matches("long"))
					type1="int";
				boolean flag = false;
				if(tablename==null ) {
					for (Iterator j = tbl.iterator();j.hasNext();){
						Tbl t1 = (Tbl)j.next();
						S = t1.getColNames().split(":");
						int i;
						for(i=0;i<S.length;i++)
							if(S[i].matches(colname))
							{
								flag = true;
								break;
							}
						if(flag==true)
						{	
							type= t1.getColTypes().split(":")[i];
							break;
						}
					}
				}
				else {
					for (Iterator j = tbl.iterator();j.hasNext();){
						Tbl t1 = (Tbl)j.next();
						if (tablename.equals(t1.getTblName()) && t1.getColNames().contains(colname) ) {
							 //we have to use split number and retrieve corresponding type n check
							int i;
							S = t1.getColNames().split(":");
							for(i=0;i<S.length;i++) {
								if(colname.matches(S[i]))
									break;
							}
							type= t1.getColTypes().split(":")[i];
							flag = true;
							break;
						}
					}
				}
				if(flag==false)
					throw (new Exception("Invalid where clause"));
				// else throw exception
			}//**************
			else if (node.getLeftChild() instanceof AttrNode  && node.getRightChild() instanceof AttrNode ){
				AttrNode anode = (AttrNode)node.getLeftChild();
				String colname = anode.getAttrName();
				String tablename = anode.getTableName();
				//-----
				AttrNode bnode = (AttrNode)node.getRightChild();
				String colname1 = bnode.getAttrName();
				String tablename1 = bnode.getTableName();
				//----------
				
				boolean flag = false;
				if(tablename==null ) {
					for (Iterator j = tbl.iterator();j.hasNext();){
						Tbl t1 = (Tbl)j.next();
						S = t1.getColNames().split(":");
						int i;
						for(i=0;i<S.length;i++)
							if(S[i].matches(colname))
							{
								flag = true;
								break;
							}
						if(flag==true)
						{	
							type= t1.getColTypes().split(":")[i];
							break;
						}
					}
				}
				else {
					for (Iterator j = tbl.iterator();j.hasNext();){
						Tbl t1 = (Tbl)j.next();
						if (tablename.equals(t1.getTblName()) && t1.getColNames().contains(colname) ) {
							 //we have to use split number and retrieve corresponding type n check
							int i;
							S = t1.getColNames().split(":");
							for(i=0;i<S.length;i++) {
								if(colname.matches(S[i]))
									break;
							}
							
							type= t1.getColTypes().split(":")[i];
							
							flag = true;
							break;
						}
					}
				}
				//-------------
				if(flag==false)
					throw (new Exception("Invalid where clause"));
				//--------
				flag=false; //reinitialization (wasted lot of time :) )
				if(tablename==null ) {
					for (Iterator j = tbl.iterator();j.hasNext();){
						Tbl t1 = (Tbl)j.next();
						S = t1.getColNames().split(":");
						int i;
						for(i=0;i<S.length;i++)
							if(S[i].matches(colname))
							{
								flag = true;
								break;
							}
						if(flag==true)
						{	
							type= t1.getColTypes().split(":")[i];
							break;
						}
					}
				}
				else {
					for (Iterator j = tbl.iterator();j.hasNext();){
						Tbl t1 = (Tbl)j.next();
						if (tablename1.equals(t1.getTblName()) && t1.getColNames().contains(colname1) ) {
							 //we have to use split number and retrieve corresponding type n check
							S = t1.getColNames().split(":");
							int i;
							for(i=0;i<S.length;i++) {
								if(colname1.matches(S[i]))
									break;
							}
							type1= t1.getColTypes().split(":")[i];
							flag = true;
							break;
						}
					}
				}
				if(flag==false)
					throw (new Exception("Invalid where clause"));
			}
			if( (!(type.matches(type1)))) {
				throw (new Exception("Invalid where clause:type miss match"));
			}
					
				
		} else {
			// if not a valid left and right child, we throw exception
			throw (new Exception("Invalid where clause"));
		}
		// recrursively iterate the tree until you visit the opnode
	
			validate(node.getLeftChild());
			//node.display();
			validate(node.getRightChild());
	}
	
		
	
	public WhereNode toCNF(WhereNode node){

        if (node.IsLeaf())
        // A
        {
            return node;
        }


        // convert children first
        WhereNode cnfLeft = null, cnfRight = null;
        if (node.getLeftChild()  != null) { cnfLeft  = toCNF(node.getLeftChild()); }
        if (node.getRightChild() != null) { cnfRight =toCNF(node.getRightChild()); }

        if (node instanceof OpNode)
        { 
        	node.setLeftChild(cnfLeft);
        	node.setRightChild(cnfRight);
        	return node; 
        	
        }
        
        if (node instanceof AndNode)
        { 
        	node.setLeftChild(cnfLeft);
        	node.setRightChild(cnfRight);
        	return node; 
        	
        }

        if (node instanceof OrNode)
        {
            if (( cnfLeft == null || cnfLeft.IsLeaf() || cnfLeft instanceof OpNode || cnfLeft instanceof AndNode)
                && (cnfRight == null || cnfRight.IsLeaf() || cnfRight instanceof OpNode || cnfRight instanceof OrNode))
            //   +
            // +   +
            {
            	node.setLeftChild(cnfLeft);
            	node.setRightChild(cnfRight);
            	return node; 
            }
            else if ((cnfLeft != null && cnfLeft instanceof AndNode)
                    && (cnfRight == null || cnfRight.IsLeaf()  || cnfRight instanceof OpNode || cnfRight instanceof OrNode))
            //   +
            // *   +
            {
            	OrNode newLeft = new OrNode();
            	newLeft.setLeftChild(cnfLeft.getLeftChild());
            	newLeft.setRightChild(cnfRight);
            	WhereNode newLeft1 = toCNF(newLeft);
                
            	OrNode newRight = new OrNode();
            	newRight.setLeftChild(cnfLeft.getRightChild());
            	newRight.setRightChild(cnfRight);
                WhereNode newRight1 = toCNF(newRight);

                AndNode node1 = new AndNode();
                node1.setLeftChild(newLeft1);
                node1.setRightChild(newRight1);
                return node1;
            }
            else if ((cnfRight != null && cnfRight instanceof AndNode)
                    && (cnfLeft == null || cnfLeft.IsLeaf() || cnfLeft instanceof OpNode || cnfLeft instanceof OrNode))
            //   +
            // +   *
            {
            	OrNode newleft = new OrNode();
            	newleft.setLeftChild(cnfLeft);
            	newleft.setRightChild(cnfRight.getRightChild());
            	WhereNode newleft1 = toCNF(newleft);
            	
               OrNode newRight = new OrNode();
               newRight.setLeftChild(cnfLeft);
               newRight.setRightChild(cnfRight.getLeftChild());
               WhereNode newright1 = toCNF(newRight);

               AndNode node1 = new AndNode();
               node1.setLeftChild(newleft1);
               node1.setRightChild(newright1);
                return node1;
            }
            else if ((cnfLeft != null && cnfLeft instanceof AndNode)
                    && (cnfRight != null && cnfRight instanceof AndNode))
            //   +
            // *   *
            {
            	AndNode newLeft = new AndNode();
            	
            	OrNode or1 = new OrNode();
            	or1.setLeftChild(cnfLeft.getLeftChild());
            	or1.setRightChild(cnfRight.getLeftChild());
            	
            	OrNode or2 = new OrNode();
            	or2.setLeftChild(cnfLeft.getRightChild());
            	or2.setRightChild(cnfRight.getLeftChild());
            	
                newLeft.setLeftChild(or1);
                newLeft.setRightChild(or2);

                WhereNode newLeft1 = toCNF(newLeft);
                
               AndNode newRight = new AndNode();
            	
            	OrNode or3 = new OrNode();
            	or3.setLeftChild(cnfLeft.getLeftChild());
            	or3.setRightChild(cnfRight.getRightChild());
            	
            	OrNode or4 = new OrNode();
            	or4.setLeftChild(cnfLeft.getRightChild());
            	or4.setRightChild(cnfRight.getRightChild());
            	
            	newRight.setLeftChild(or1);
            	newRight.setRightChild(or2);

                WhereNode newRight1 = toCNF(newRight);
                
                AndNode node1 = new AndNode();
                node1.setLeftChild(newLeft1);
                node1.setRightChild(newRight1);

                return node1;
            }
        }

        // error status, should NOT reach here
        System.out.println("Error Status");
        return null;
		
	}
	public ArrayList<JoinExpression> getJelist() {
		return jelist;
	}
	public void setJelist(ArrayList<JoinExpression> jelist) {
		this.jelist = jelist;
	}
	public ArrayList<SimpleExpression> getSelist() {
		return selist;
	}
	public void setSelist(ArrayList<SimpleExpression> selist) {
		this.selist = selist;
	}

}