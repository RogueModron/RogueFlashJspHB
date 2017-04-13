package app.model.entities;

import app.model.planner.ReviewValues;
import java.time.OffsetDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CardReview.class)
public abstract class CardReview_ {

	public static volatile SingularAttribute<CardReview, OffsetDateTime> dateTime;
	public static volatile SingularAttribute<CardReview, CardInstance> instance;
	public static volatile SingularAttribute<CardReview, Integer> id;
	public static volatile SingularAttribute<CardReview, ReviewValues> value;

}

