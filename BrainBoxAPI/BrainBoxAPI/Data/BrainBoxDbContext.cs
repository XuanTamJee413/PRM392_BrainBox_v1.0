using BrainBoxAPI.Models;
using Microsoft.EntityFrameworkCore;

namespace BrainBoxAPI.Data
{
    public class BrainBoxDbContext : DbContext
    {
        public BrainBoxDbContext(DbContextOptions<BrainBoxDbContext> options) : base(options) { }

        public DbSet<User> Users { get; set; }
        public DbSet<Quiz> Quizzes { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            ConfigureEntities(modelBuilder);
            SeedData(modelBuilder);
        }
        private void ConfigureEntities(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Quiz>()
                .HasOne(q => q.User)
                .WithMany()
                .HasForeignKey(q => q.CreatorId)
                .OnDelete(DeleteBehavior.Cascade);
            modelBuilder.Entity<User>()
                .HasMany<Quiz>()
                .WithOne()
                .HasForeignKey(q => q.CreatorId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<User>()
                .HasMany<Document>()
                .WithOne(d => d.User!)
                .HasForeignKey(d => d.AuthorId)
                .OnDelete(DeleteBehavior.Cascade);
        }

        private void SeedData(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<User>().HasData(
                new User { Id = 1, Username = "admin", Password = "123456", Role = "admin", Email = "admin@brainbox.com", Status = true, Avatar = "", CreatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(), PremiumExpiredAt = 0 },
                new User { Id = 2, Username = "user1", Password = "123456", Role = "user", Email = "user1@brainbox.com", Status = true, Avatar = "", CreatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(), PremiumExpiredAt = 0 }
            );

            modelBuilder.Entity<Quiz>().HasData(
                new Quiz { QuizId = 1, QuizName = "Basic Math", Description = "Simple arithmetic quiz", CreatorId = 1, IsPublic = true, CreatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() },
                new Quiz { QuizId = 2, QuizName = "English Vocabulary", Description = "Common English words", CreatorId = 2, IsPublic = true, CreatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() }
            );
            modelBuilder.Entity<Document>().HasData(
                new Document { DocId = 1, Title = "Intro to Java", Content = "Java basics", AuthorId = 1, IsPublic = true, Views = 10, CreatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() },
                new Document { DocId = 2, Title = "ASP.NET Guide", Content = "How to use OData", AuthorId = 2, IsPublic = false, Views = 5, CreatedAt = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() }
            );

        }

    }
}
