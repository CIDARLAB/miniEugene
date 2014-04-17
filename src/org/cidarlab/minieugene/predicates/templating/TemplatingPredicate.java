package org.cidarlab.minieugene.predicates.templating;

import java.util.List;
import java.util.UUID;

import org.cidarlab.minieugene.dom.Component;
import org.cidarlab.minieugene.predicates.Predicate;

public abstract class TemplatingPredicate 
	extends Predicate {
	
	private String name;
	private List<List<Component>> components;
	private boolean negated;
	
	/**
	 * 
	 * @param name
	 * @param components
	 * @param isNegated
	 */
	TemplatingPredicate(String name, List<List<Component>> components, boolean isNegated) {

		if(null != name && !name.isEmpty()) {
			this.name = name;
		} else {
			this.name = UUID.randomUUID().toString();
		}
		
		this.components = components;
		this.negated = isNegated;
	}

	/**
	 * 
	 */
	public void setNegated() {
		this.negated = true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isNegated() {
		return this.negated;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<List<Component>> getComponents() {
		return this.components;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

}