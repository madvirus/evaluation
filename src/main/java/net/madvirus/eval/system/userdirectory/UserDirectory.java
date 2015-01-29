package net.madvirus.eval.system.userdirectory;

import net.madvirus.eval.infra.jpa.BooleanTFConverter;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "directory_type")
@Table(name = "user_directory")
public abstract class UserDirectory {
    @Id
    @Column(name = "user_directory_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "internal")
    @Convert(converter = BooleanTFConverter.class)
    private Boolean internal;

    @Transient
    protected UserModelRepository userModelRepository;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_directory_config", joinColumns = @JoinColumn(name = "user_directory_id"))
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    protected Map<String, String> config;

    public UserDirectory() {
    }

    public UserDirectory(String name, Map<String, String> config) {
        this.name = name;
        this.config = config;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean isInternal() {
        return internal;
    }

    public abstract UserModel authenticate(String userId, String userPassword);

    @Autowired
    public void setUserModelRepository(UserModelRepository userModelRepository) {
        this.userModelRepository = userModelRepository;
    }

}
