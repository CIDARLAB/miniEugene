package org.cidarlab.minieugene.predicates.templating;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.cidarlab.minieugene.constants.RuleOperator;
import org.cidarlab.minieugene.dom.Component;
import org.cidarlab.minieugene.exception.EugeneException;
import org.cidarlab.minieugene.solver.jacop.Variables;

import JaCoP.constraints.And;
import JaCoP.constraints.Not;
import JaCoP.constraints.Or;
import JaCoP.constraints.PrimitiveConstraint;
import JaCoP.constraints.XeqC;
import JaCoP.core.IntVar;
import JaCoP.core.Store;

public class Sequence 
	extends TemplatingPredicate {
		
	public Sequence() {
		super(null, new ArrayList<List<Component>>(), false);
	}
	
	public Sequence(String name) {
		super(name, new ArrayList<List<Component>>(), false);
	}
	
	public Sequence(String name, List<List<Component>> components) {
		super(name, components, false);
	}
	
	@Override
	public String getOperator() {
		return RuleOperator.SEQUENCE.toString();
	}

	@Override
	public PrimitiveConstraint toJaCoP(Store store, IntVar[][] variables)
			throws EugeneException {
		
		if(this.isNegated()) {
			return this.toJaCoPNot(store, variables);
		}
		
		int maxN = variables[Variables.PART].length;
		
		return createSequence(variables, maxN);
	}
	
	@Override
	public PrimitiveConstraint toJaCoPNot(Store store, IntVar[][] variables)
			throws EugeneException {
		int maxN = variables[Variables.PART].length;
		
		return new Not(
				createSequence(variables, maxN));
	}
	
	private PrimitiveConstraint createSequence(IntVar[][] variables, int maxN) {
		
		PrimitiveConstraint[] pc = null;
		for(int i=0; i + this.getComponents().size() <= maxN; i++) {
			
			List<Component> lst_ci = this.getComponents().get(0);
			
			PrimitiveConstraint[] pcTemplate = new PrimitiveConstraint[this.getComponents().size() - 1];
			for(int j=1; j<this.getComponents().size(); j++) {
				List<Component> lst_cj = this.getComponents().get(j);
				
				PrimitiveConstraint[] pcSelection = new PrimitiveConstraint[lst_cj.size()];
				int k=0;
				for(Component cj : this.getComponents().get(j)) {
					pcSelection[k++] = new XeqC(variables[Variables.PART][j+i], cj.getId());
				}

				pcTemplate[j-1] = new Or(pcSelection);

			}
			
			// SELECTION
			PrimitiveConstraint[] pcSelection = new PrimitiveConstraint[lst_ci.size()];
			int k=0;
			for(Component ci : lst_ci) {
				pcSelection[k++] = new XeqC(variables[Variables.PART][i], ci.getId());
			}

			if(pc == null) {
				pc = new PrimitiveConstraint[1];
				pc[0] =	new And(
							new Or(pcSelection),
							new And(pcTemplate)); 
			} else {
				pc = ArrayUtils.add(pc, 
					new And(
							new Or(pcSelection),
						new And(pcTemplate)));
			}
		}

		return new Or(pc);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(this.isNegated()) {
			sb.append("NOT ");
		}
		
		if(null != this.getName() && !this.getName().isEmpty()) {
			sb.append(this.getName()).append(" ");
		}

		sb.append(this.getOperator()).append(" ");
		for(int i=0; i<this.getComponents().size(); i++) {
			
			// SELECTION
			sb.append("[");
			List<Component> lst_selection = this.getComponents().get(i);
			for(int j=0; j<lst_selection.size(); j++) {	
				sb.append(lst_selection.get(j).getName());
				
				if(j < lst_selection.size() - 1) {
					sb.append("|");
				}
			}
					
			sb.append("]");
			
			if(i<this.getComponents().size()-1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
}
