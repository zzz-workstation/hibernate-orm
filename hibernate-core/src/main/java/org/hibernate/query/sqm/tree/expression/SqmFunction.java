/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.query.sqm.tree.expression;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.criteria.JpaFunction;
import org.hibernate.query.hql.spi.SemanticPathPart;
import org.hibernate.query.hql.spi.SqmCreationState;
import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.SemanticQueryWalker;
import org.hibernate.query.sqm.SqmExpressable;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.query.sqm.sql.internal.DomainResultProducer;
import org.hibernate.query.sqm.tree.SqmNode;
import org.hibernate.query.sqm.tree.SqmVisitableNode;
import org.hibernate.query.sqm.tree.domain.SqmPath;

/**
 * An SQM function
 *
 * @author Steve Ebersole
 */
public class SqmFunction<T> extends AbstractSqmExpression<T> implements JpaFunction<T>, DomainResultProducer<T>, SemanticPathPart {
	// this function-name is the one used to resolve the descriptor from the function registry (which may or may not be a db function name)
	private final String functionName;
	private final SqmFunctionDescriptor functionDescriptor;

	private List<SqmVisitableNode> arguments;

	public SqmFunction(
			String functionName,
			SqmFunctionDescriptor functionDescriptor,
			SqmExpressable<T> type,
			NodeBuilder criteriaBuilder) {
		super( type, criteriaBuilder );
		this.functionName = functionName;
		this.functionDescriptor = functionDescriptor;
	}

	public SqmFunction(
			String functionName,
			SqmFunctionDescriptor functionDescriptor,
			SqmExpressable<T> type,
			List<SqmVisitableNode> arguments,
			NodeBuilder criteriaBuilder) {
		super( type, criteriaBuilder );
		this.functionName = functionName;
		this.functionDescriptor = functionDescriptor;
		this.arguments = arguments;
	}

	@Override
	public String getFunctionName() {
		return functionName;
	}

	public List<SqmVisitableNode> getArguments() {
		return arguments;
	}

	public void addArgument(SqmVisitableNode argument) {
		assert argument != null;

		if ( arguments == null ) {
			arguments = new ArrayList<>();
		}
		arguments.add( argument );
	}

	@Override
	public <X> X accept(SemanticQueryWalker<X> walker) {
		return walker.visitFunction( this );
	}


	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// SemanticPathPart


	@Override
	public SemanticPathPart resolvePathPart(
			String name,
			boolean isTerminal,
			SqmCreationState creationState) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SqmPath resolveIndexedAccess(
			SqmExpression selector,
			boolean isTerminal,
			SqmCreationState creationState) {
		throw new UnsupportedOperationException();
	}
}