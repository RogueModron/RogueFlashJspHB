package app.model.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Deck.class)
public abstract class Deck_ {

	public static volatile SingularAttribute<Deck, String> notes;
	public static volatile ListAttribute<Deck, Card> cards;
	public static volatile SingularAttribute<Deck, String> description;
	public static volatile SingularAttribute<Deck, Integer> id;
	public static volatile SingularAttribute<Deck, Integer> version;

}

