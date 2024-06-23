package pixels.pro.fit.entity;

import pixels.pro.fit.dao.entity.UserPrincipal;
import pixels.pro.fit.testutil.Builder;

public class UserTestBuilder implements Builder<UserPrincipal> {
    private String username;
    private String password;

    private UserTestBuilder(UserTestBuilder builder){
        this.username = builder.username;
        this.password = builder.password;
    }

    private UserTestBuilder() {}

    public static UserTestBuilder aUser(){
        return new UserTestBuilder();
    }

    public UserTestBuilder withUsername(String username){
        UserTestBuilder copy= new UserTestBuilder(this);
        this.username = username;
        return this;
    }

    public UserTestBuilder withPassword(String password){
        UserTestBuilder copy= new UserTestBuilder(this);
        this.password = password;
        return this;
    }

    @Override
    public UserPrincipal build() {
        UserPrincipal user = new UserPrincipal();
        user.setPassword(password);
        user.setUsername(username);
        return user;
    }
}
