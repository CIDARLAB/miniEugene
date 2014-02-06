/*
Copyright (c) 2014 Boston University.
All rights reserved.
Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the above
copyright notice and the following two paragraphs appear in all copies
of this software.

IN NO EVENT SHALL BOSTON UNIVERSITY BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
BOSTON UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

BOSTON UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND BOSTON UNIVERSITY HAS
NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS.
 */

package org.cidarlab.minieugene;

import org.cidarlab.minieugene.solver.jacop.PartTypes;

/**
 * A symbol represents a rule operand in miniEugene. From a biology perspective, 
 * a symbol can represent either a nucleotide, part, device, or systems. 
 * I.e. a symbol is agnostic to the level of abstraction.
 * <p>
 * A symbol is characterized by four attributes:<br/>
 * - an integer ID (generated by miniEugene)<br/>
 * - a unique name<br/>
 * - a type<br/>
 * - direction (forward/reverse)<br/>
 * <p>
 * The type is automatically resolved from the first character of the symbol's name.<br/>
 * p: Promoter, r: RBS, c or g: Gene, t: Terminator<br/>
 * For example, naming a rule operand pTet, miniEugene will infer that pTet is a Promoter.
 * <p>  
 * If not specified, every symbol has a forward orientation.  
 * <p>
 * @author Ernst Oberortner
 */
public class Symbol {

	/*
	 * id
	 */
	private int id;
	
	/*
	 * name
	 */
	private String name;
	
	/*
	 * direction
	 */
	private boolean forward;
	
	public Symbol(String name) {
		this.name = name;
		this.forward = true;

		this.id = this.name.hashCode();
	}
	
	public Symbol(String name, boolean forward) {
		this.name = name;
		this.forward = forward;
		
		this.id = this.name.hashCode();
	}
	
	/**
	 * isForward/0 returns true if the symbol's orientation is forward, 
	 * false otherwise. 
	 * 
	 * @return true    if the symbol has a forward orientation
	 *         false   otherwise
	 */
	public boolean isForward() {
		return forward;
	}
	
	/**
	 * setForward/1 is used by miniEugene internally to set the 
	 * correct orientation of a symbol in a solution.
	 * <p> 
	 * It is up to you to change the orientation of a symbol while 
	 * processing the solutions.
	 *  
	 * @param forward <br/>
	 * true if the symbol is forward oriented<br/>
	 * false, otherwise
	 */
	public void setForward(boolean forward) {
		this.forward = forward;
	}

	/**
	 * getName/0 returns the name of the symbol
	 * 
	 * @return String ... the name of the symbol
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * getID/0 returns the ID of the symbol
	 * 
	 * @return int ... the automatically generated ID of the symbol
	 */
	public int getId() {
		return this.id;
	}

	public int getType() {
		if('p' == this.name.charAt(0) || 'P' == this.name.charAt(0)) {
			return PartTypes.get("PROMOTER");
		} else if('r' == this.name.charAt(0) || 'R' == this.name.charAt(0)) {
			return PartTypes.get("RBS");  
		} else if('c' == this.name.charAt(0) || 'C' == this.name.charAt(0)) {
			return PartTypes.get("GENE");  
		} else if('t' == this.name.charAt(0) || 'T' == this.name.charAt(0)) {
			return PartTypes.get("TERMINATOR");  
		} 
		return 5;
	}
	
	/*
	private String toPigeon() {
		if(!this.isForward()) {
			return "<"+this.getName();
		}
		return this.getName();
	}
	*/
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(this.getId()).append(", ").append(this.getName()).append(", ");
		if(this.isForward()) {
			sb.append("->");
		} else {
			sb.append("<-");
		}
		sb.append("}");
		return sb.toString();
	}
}