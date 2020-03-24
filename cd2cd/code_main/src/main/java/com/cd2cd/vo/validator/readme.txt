# springboot使用hibernate validator校验
	https://www.cnblogs.com/mr-yang-localhost/p/7812038.html
	官方提供完整的方式：
		http://hibernate.org/validator/documentation/
		
	JSR 380		
	
	自定义验证：
	https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-customconstraints
	
	private Set<@ValidPart String> parts = new HashSet<>();
	private List<@ValidPart String> parts = new ArrayList<>();
	private Map<@NotNull FuelConsumption, @MaxAllowedFuelConsumption Integer> fuelConsumption = new HashMap<>();
	private Optional<@MinTowingCapacity(1000) Integer> towingCapacity = Optional.empty();
	private Map<@NotNull Part, List<@NotNull Manufacturer>> partManufacturers = new HashMap<>();
	@ValidPassengerCount
	public class Car {  }
	
	@NotNull
    @Valid
    private Person driver;
    
    private List<@NotNull @Valid Person> passengers = new ArrayList<Person>();
    
    private Map<@Valid Part, List<@Valid Manufacturer>> partManufacturers = new HashMap<>();
    
    
    /**
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	validator = factory.getValidator();
	
	Car car = new Car( null, true );
	Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

	assertEquals( 1, constraintViolations.size() );
	assertEquals( "must not be null", constraintViolations.iterator().next().getMessage() );
    **/
    
    validator.validate( car );
    validator.validateProperty( car, "manufacturer" );
    validator.validateValue( Car.class, "manufacturer", null );
    
    所有属性级别验证Api列表
    https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-defineconstraints-spec
    
    @AssertFalse
    @AssertTrue
    @DecimalMax(value=, inclusive=)
    	Supported data types: BigDecimal, BigInteger, CharSequence, byte, short, int, long
    
    @DecimalMin(value=, inclusive=)
    @Digits(integer=, fraction=)
    @Email
    @Future  : Checks whether the annotated date is in the future
    	Date Calendar Instant LocalDate LocalDateTime LocalTime MonthDay 
    	OffsetDateTime OffsetTime Year YearMonth ZonedDateTime HijrahDate
    	
    @FutureOrPresent : Checks whether the annotated date is in the present or in the future
    @Max(value=)
    @Min(value=)
    @NotBlank
    	type: CharSequence
    @NotEmpty : Checks whether the annotated element is not null nor empty
    	type: CharSequence, Collection, Map and arrays
    	
    @NotNull : Checks that the annotated value is not null
		Supported data types Any type
	
	@Negative 负数
	@NegativeOrZero 小于等零
	@Null
	
	@Past : date Checks whether the annotated date is in the past
	@PastOrPresent 过去或现在
	
	正则匹配
	@Pattern(regex=, flags=)
	
	@Positive 正数
	@PositiveOrZero
	@Size(min=, max=) Checks if the annotated element’s size is between min and max (inclusive)
		type: CharSequence, Collection, Map and arrays
	
	@CreditCardNumber(ignoreNonDigitCharacters=) : 信用卡号码
	http://www.dirigodev.com/blog/ecommerce/anatomy-of-a-credit-card-number/
	
	@Currency(value=) 货币
	
	@DurationMax(days=, hours=, minutes=, seconds=, millis=, nanos=, inclusive=)
		type: java.time.Duration
	
	@EAN
	@ISBN
	@Length(min=, max=)
	@CodePointLength(min=, max=, normalizationStrategy=)
	
	@LuhnCheck(startIndex= , endIndex=, checkDigitIndex=, ignoreNonDigitCharacters=)
	@Mod10Check(multiplier=, weight=, startIndex=, endIndex=, checkDigitIndex=, ignoreNonDigitCharacters=)
	
	@Range(min=, max=)
	
	@SafeHtml(whitelistType= , additionalTags=, additionalTagsWithAttributes=, baseURI=)
    	
    @ScriptAssert(lang=, script=, alias=, reportOn=)
    @UniqueElements	
    	Supported data types ： Collection
    	
    @URL(protocol=, host=, port=, regexp=, flags=)
    	
## Country specific constraints
 	@CNPJ
 	@CPF
 	@TituloEleitoral
 	@NIP
 	@PESEL
 	@REGON
 	
## Declaring and validating method constraints
	
	
	 Parameter constraints
	 public class RentalStation {

	    public RentalStation(@NotNull String name) {
	        //...
	    }
	
	    public void rentCar(
	            @NotNull Customer customer,
	            @NotNull @Future Date startDate,
	            @Min(1) int durationInDays) {
	        //...
	    }
	}	 	    	
    	
	中文讲解    	
	http://jinnianshilongnian.iteye.com/blog/1990081
	    	
    	
    	