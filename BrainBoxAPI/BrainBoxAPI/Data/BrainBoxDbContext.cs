using BrainBoxAPI.Models;
using Microsoft.EntityFrameworkCore;

namespace BrainBoxAPI.Data
{
    public class BrainBoxDbContext : DbContext
    {
        public BrainBoxDbContext(DbContextOptions<BrainBoxDbContext> options) : base(options)
        {
        }

        public DbSet<User> Users { get; set; }
        public DbSet<Document> Documents { get; set; }
        public DbSet<Tag> Tags { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            
        }
    }
}
