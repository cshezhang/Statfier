
public class MyEntity {

    private static final String MY_STRING = "STRING";

    @Id
    @Column
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn
    @Valid
    private RelationEntity relation;

    public MyEntity() {
    }
}
        