package app.model.entities;

import java.time.OffsetDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CardPlan.class)
public abstract class CardPlan_ {

	public static volatile SingularAttribute<CardPlan, CardInstance> instance;
	public static volatile SingularAttribute<CardPlan, Integer> nextDays;
	public static volatile SingularAttribute<CardPlan, Integer> lastDays;
	public static volatile SingularAttribute<CardPlan, Integer> id;
	public static volatile SingularAttribute<CardPlan, OffsetDateTime> nextDate;
	public static volatile SingularAttribute<CardPlan, OffsetDateTime> lastDate;

}

