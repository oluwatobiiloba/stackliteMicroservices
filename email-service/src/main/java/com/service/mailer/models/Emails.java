package com.service.mailer.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Table(name = "email_templates")
@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Emails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "html_content", nullable = false)
    private String htmlContent;
}
