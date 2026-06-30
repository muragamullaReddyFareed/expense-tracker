package com.expensetracker.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "categories")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) private User user;
    @Column(nullable = false, length = 50) private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10) private TransactionType type;
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); }
    public Category() {}
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getName() { return name; }
    public TransactionType getType() { return type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setId(Long id) { this.id = id; }
    public void setUser(User u) { this.user = u; }
    public void setName(String n) { this.name = n; }
    public void setType(TransactionType t) { this.type = t; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private User user; private String name; private TransactionType type;
        public Builder id(Long id){this.id=id;return this;}
        public Builder user(User u){this.user=u;return this;}
        public Builder name(String n){this.name=n;return this;}
        public Builder type(TransactionType t){this.type=t;return this;}
        public Category build(){Category c=new Category();c.id=id;c.user=user;c.name=name;c.type=type;return c;}
    }
}