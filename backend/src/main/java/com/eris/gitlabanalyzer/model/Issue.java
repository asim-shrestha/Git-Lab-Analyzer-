package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Issue")
@Table(name = "issue")
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.ToString
public class Issue {
    @Id
    @SequenceGenerator(
            name = "issue_sequence",
            sequenceName = "issue_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "issue_sequence"
    )
    @Column(
            name = "issue_id"
    )
    private Long id;

    @Column(
            name = "issue_iid",
            nullable = false
    )
    private Long iid;

    @Column(
            name = "title",
            nullable = false

    )
    private String title;

    @Column(
            name = "author_name",
            nullable = false

    )
    private String authorName;

    @Column(
            name = "created_at",
            nullable = false

    )
    private OffsetDateTime createdAt;

    @Column(
            name = "web_url",
            nullable = false

    )
    private String webUrl;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            referencedColumnName = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "git_management_user_id",
            nullable = false,
            referencedColumnName = "git_management_user_id")
    private GitManagementUser gitManagementUser;

    public Issue(Long iid, String title, String authorName, OffsetDateTime createdAt, String webUrl, Project project, GitManagementUser gitManagementUser) {
        this.iid = iid;
        this.title = title;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
        this.gitManagementUser = gitManagementUser;
    }
}
