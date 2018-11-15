Example of use:

/* the BaseEntity is not a TABLE, but all classes can extend it */
public abstract class BaseEntity {

	@SQLIdentifier(identifierName = "ID")
	protected Long id;
 
 // assume getters and setters
 
 }

// we can set the own name of the primary key, if we do not use it, the ID name of BaseEntity will prevail
 
@SQLTable(table = "TB_USER", dropTableIfExists = true)
@SQLInheritancePK(primaryKeyName = "USER_ID")
public class User extends BaseEntity{

  // in mysql 
  // NAME VARCHAR(600) NOT NULL
	@SQLColumn(name = "NAME", required = true, length = 60)
	private String name;
	
	@SQLColumn(name = "LOGIN", required = true, length = 32)
	private String login;

	@SQLColumn(name = "PASSWORD", required = true, length = 32)
	private String password;
  
  // the use of @SQLColumn is optional, 
  // if we do not use, we get default size of the field
  // SALARY DECIMAL(5,2) 
  private BigDecimal salary;
  
  // if we do not need a column to a field use:
  @SQLIgnore
  private int ignoreIt;
  
  }
