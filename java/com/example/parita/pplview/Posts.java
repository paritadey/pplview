package com.example.parita.pplview;

/**
 * Created by parita on 26-02-2018.
 */

public class Posts {
    String heading, description, source, timeofpost, authoremail, authorname;
    public Posts(String heading, String description, String source, String timeofpost, String authorname, String authoremail){
        this.heading=heading;this.description=description;
        this.source=source;this.timeofpost=timeofpost;
        this.authorname=authorname; this.authoremail=authoremail;
    }
    public String getHeading_post(){return heading;}
    public String getDescription(){return description;}
    public String getSource(){return source;}
    public String getTimeofpost(){return timeofpost;}
    public String getAuthorname(){return authorname;}
    public String getAuthoremail(){return authoremail;}
}
