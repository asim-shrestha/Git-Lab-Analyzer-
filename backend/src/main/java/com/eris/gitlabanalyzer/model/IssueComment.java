package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "IssueComment")
@Table(name = "issue_comment")
public class IssueComment {
    @Id
    @SequenceGenerator(
            name = "issue_comment_sequence",
            sequenceName = "issue_comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "issue_comment_sequence"
    )
    @Column(
            name = "issue_comment_id"
    )
    private Long id;

    @Column(
            name = "issue_comment_iid",
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
            name = "issue_id",
            nullable = false,
            referencedColumnName = "issue_id")
    private Issue issue;

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

    public IssueComment() {
    }

    public IssueComment(Long iid, GitLabUser gitLabUser, Issue issue, String body, String webUrl, String createdAt) {
        this.iid = iid;
        this.gitLabUser = gitLabUser;
        this.issue = issue;
        this.body = body;
        this.webUrl = webUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getIid() {
        return iid;
    }

    public GitLabUser getGitLabUser() {
        return gitLabUser;
    }

    public Issue getIssue() {
        return issue;
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

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return "IssueComment{" +
                "id=" + id +
                ", iid=" + iid +
                ", issue=" + issue +
                ", body='" + body + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
