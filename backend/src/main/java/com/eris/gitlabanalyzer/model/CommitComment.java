package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "CommitComment")
@Table(name = "commit_comment")
public class CommitComment {

    @Id
    @SequenceGenerator(
            name = "commit_comment_sequence",
            sequenceName = "commit_comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "commit_comment_sequence"
    )
    @Column(
            name = "commit_comment_id"
    )
    private Long id;

    @Column(
            name = "commit_comment_iid",
            nullable = false
    )
    private Long iid;

    @ManyToOne
    @JoinColumn(
            name = "gitlab_user_id",
            nullable = false,
            referencedColumnName = "gitlab_user_id")
    private GitLabUser gitLabUser;

    @ManyToOne
    @JoinColumn(
            name = "commit_id",
            nullable = false,
            referencedColumnName = "commit_id")
    private Commit commit;

    @Column(
            name = "body",
            columnDefinition="TEXT",
            nullable = false

    )
    private String body;

    @Column(
            name = "web_Url",
            nullable = false

    )
    private String webUrl;

    @Column(
            name = "created_at",
            nullable = false

    )
    private String createdAt;

    public Long getId() {
        return id;
    }

    public GitLabUser getMember() {
        return gitLabUser;
    }

    public String getBody() {
        return body;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setMember(GitLabUser gitLabUser) {
        this.gitLabUser = gitLabUser;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public CommitComment() {
    }

    public CommitComment(Long id, GitLabUser gitLabUser, String body, String webUrl, String createdAt) {
        this.id = id;
        this.gitLabUser = gitLabUser;
        this.body = body;
        this.webUrl = webUrl;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CommitComment{" +
                "id=" + id +
                ", iid=" + iid +
                ", body='" + body + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
