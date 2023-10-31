import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/")
public class JaxRsAnnotatedController {


    @Autowired
    private SessionFactory sessionFactory;

    @GET
    @Path("/1/{name}")
    public Response getInfoUser(@PathParam("name") String name) {
        List<CommentDto> comments = sessionFactory.openSession().createQuery("FROM comment WHERE user='" + name + "'").list();
        return Response.status(Response.Status.OK).entity(comments).build();
    }

}

class CommentDto {
    private String user;
    private String email;
    private String avatarUrl;
    private String content;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
