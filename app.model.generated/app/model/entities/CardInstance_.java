package app.model.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CardInstance.class)
public abstract class CardInstance_ {

	public static volatile ListAttribute<CardInstance, CardReview> reviews;
	public static volatile SingularAttribute<CardInstance, Boolean> sideAToB;
	public static volatile SingularAttribute<CardInstance, Boolean> disabled;
	public static volatile SingularAttribute<CardInstance, Integer> id;
	public static volatile SingularAttribute<CardInstance, CardPlan> plan;
	public static volatile SingularAttribute<CardInstance, Card> card;

}

