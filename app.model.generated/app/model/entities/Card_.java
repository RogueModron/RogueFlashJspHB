package app.model.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Card.class)
public abstract class Card_ {

	public static volatile SingularAttribute<Card, String> notes;
	public static volatile ListAttribute<Card, CardInstance> instances;
	public static volatile SingularAttribute<Card, Deck> deck;
	public static volatile SingularAttribute<Card, Integer> id;
	public static volatile SingularAttribute<Card, Integer> version;
	public static volatile SingularAttribute<Card, String> sideB;
	public static volatile SingularAttribute<Card, String> sideA;
	public static volatile SingularAttribute<Card, String> tags;

}

