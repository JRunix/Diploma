package com.rx.dao;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Discipline {

    @Id
    @GeneratedValue
    private Long disciplineId;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Document> curriculums;

    @ManyToMany(mappedBy = "discipline")
    private Set<User> users;


    protected Discipline() {
    }

    private Discipline(DisciplineBuilder builder) {
        this.name = builder.name;
        this.curriculums = builder.curriculums;
    }

    public static DisciplineBuilder builder() {
        return new DisciplineBuilder();
    }

    public Set<Document> getCurriculums() {
        return curriculums;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public static class DisciplineBuilder {
        private String name;

        private Set<Document> curriculums;


        private DisciplineBuilder() {
            this.curriculums = new HashSet<>();
        }

        public DisciplineBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DisciplineBuilder withCik(Document cik) {
            this.curriculums.add(cik);
            return this;
        }

        public Discipline build() {
            return new Discipline(this);
        }
    }
}