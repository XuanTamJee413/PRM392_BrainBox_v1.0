using BrainBoxAPI.Models;
using BrainBoxAPI.Utilities;
using Microsoft.EntityFrameworkCore;

namespace BrainBoxAPI.Data
{
    public class BrainBoxDbContext : DbContext
    {
        public BrainBoxDbContext(DbContextOptions<BrainBoxDbContext> options) : base(options) { }

        public DbSet<Bookmark> Bookmarks { get; set; }
        public DbSet<Challenge> Challenges { get; set; }
        public DbSet<Comment> Comments { get; set; }
        public DbSet<Document> Documents { get; set; }
        public DbSet<DocumentDetail> DocumentDetails { get; set; }
        public DbSet<DocumentTagCrossRef> DocumentTagCrossRefs { get; set; }
        public DbSet<DownloadHistory> DownloadHistories { get; set; }
        public DbSet<Flashcard> Flashcards { get; set; }
        public DbSet<Notification> Notifications { get; set; }
        public DbSet<Quiz> Quizzes { get; set; }
        public DbSet<RatingQuiz> RatingQuizzes { get; set; }
        public DbSet<Tag> Tags { get; set; }
        public DbSet<User> Users { get; set; }


        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            ConfigureEntities(modelBuilder);
            SeedData(modelBuilder);
        }
        private void ConfigureEntities(ModelBuilder modelBuilder)
        {
            // Composite keys
            modelBuilder.Entity<Bookmark>().HasKey(b => new { b.UserId, b.QuizId });
            modelBuilder.Entity<DownloadHistory>().HasKey(h => new { h.UserId, h.TargetId, h.TargetType });
            modelBuilder.Entity<DocumentTagCrossRef>().HasKey(x => new { x.DocId, x.TagId });

            // Quiz → User (Creator)
            modelBuilder.Entity<Quiz>()
                .HasOne(q => q.Creator)
                .WithMany(u => u.Quizzes)
                .HasForeignKey(q => q.CreatorId)
                .OnDelete(DeleteBehavior.Restrict);

            // Document → User (Author)
            modelBuilder.Entity<Document>()
                .HasOne(d => d.Author)
                .WithMany(u => u.Documents)
                .HasForeignKey(d => d.AuthorId)
                .OnDelete(DeleteBehavior.Restrict);

            // DocumentDetail → Document
            modelBuilder.Entity<DocumentDetail>()
                .HasOne(d => d.Document)
                .WithMany(d => d.DocumentDetails)
                .HasForeignKey(d => d.DocId)
                .OnDelete(DeleteBehavior.Cascade);

            // Bookmark → User
            modelBuilder.Entity<Bookmark>()
                .HasOne(b => b.User)
                .WithMany()
                .HasForeignKey(b => b.UserId)
                .OnDelete(DeleteBehavior.Restrict);

            // Bookmark → Quiz
            modelBuilder.Entity<Bookmark>()
                .HasOne(b => b.Quiz)
                .WithMany()
                .HasForeignKey(b => b.QuizId)
                .OnDelete(DeleteBehavior.Restrict);

            // Bookmark → Flashcard
            modelBuilder.Entity<Bookmark>()
                .HasOne(b => b.Flashcard)
                .WithMany()
                .HasForeignKey(b => b.LastCardId)
                .OnDelete(DeleteBehavior.SetNull);

            // Challenge → Quiz
            modelBuilder.Entity<Challenge>()
                .HasOne(c => c.Quiz)
                .WithMany()
                .HasForeignKey(c => c.QuizId)
                .OnDelete(DeleteBehavior.Cascade);

            // Challenge → Challenger
            modelBuilder.Entity<Challenge>()
                .HasOne(c => c.Challenger)
                .WithMany()
                .HasForeignKey(c => c.ChallengerId)
                .OnDelete(DeleteBehavior.Restrict);

            // Challenge → Opponent
            modelBuilder.Entity<Challenge>()
                .HasOne(c => c.Opponent)
                .WithMany()
                .HasForeignKey(c => c.OpponentId)
                .OnDelete(DeleteBehavior.Restrict);

            // Comment → User
            modelBuilder.Entity<Comment>()
                .HasOne(c => c.User)
                .WithMany()
                .HasForeignKey(c => c.UserId)
                .OnDelete(DeleteBehavior.Restrict);

            // Comment → DocumentDetail
            modelBuilder.Entity<Comment>()
                .HasOne(c => c.DocumentDetail)
                .WithMany()
                .HasForeignKey(c => c.DocDetailId)
                .OnDelete(DeleteBehavior.Cascade);

            // Notification → User
            modelBuilder.Entity<Notification>()
                .HasOne(n => n.User)
                .WithMany()
                .HasForeignKey(n => n.UserId)
                .OnDelete(DeleteBehavior.Restrict);

            // Flashcard → Quiz
            modelBuilder.Entity<Flashcard>()
                .HasOne(f => f.Quiz)
                .WithMany()
                .HasForeignKey(f => f.QuizId)
                .OnDelete(DeleteBehavior.Cascade);

            // RatingQuiz → Quiz
            modelBuilder.Entity<RatingQuiz>()
                .HasOne(r => r.Quiz)
                .WithMany()
                .HasForeignKey(r => r.QuizId)
                .OnDelete(DeleteBehavior.Cascade);

            // RatingQuiz → User
            modelBuilder.Entity<RatingQuiz>()
                .HasOne(r => r.User)
                .WithMany()
                .HasForeignKey(r => r.UserId)
                .OnDelete(DeleteBehavior.Restrict);

            // DownloadHistory → User
            modelBuilder.Entity<DownloadHistory>()
                .HasOne(d => d.User)
                .WithMany()
                .HasForeignKey(d => d.UserId)
                .OnDelete(DeleteBehavior.Restrict);

            // DocumentTagCrossRef → Document
            modelBuilder.Entity<DocumentTagCrossRef>()
                .HasOne(x => x.Document)
                .WithMany()
                .HasForeignKey(x => x.DocId)
                .OnDelete(DeleteBehavior.Cascade);

            // DocumentTagCrossRef → Tag
            modelBuilder.Entity<DocumentTagCrossRef>()
                .HasOne(x => x.Tag)
                .WithMany()
                .HasForeignKey(x => x.TagId)
                .OnDelete(DeleteBehavior.Cascade);
        }


        private void SeedData(ModelBuilder modelBuilder)
        {
            //const long now = 1748620800000; // hard code ngay` 1/6/2025 00:00:00 GMT+7
            long now = DateUtils.DateTimeToTimestamp("01/06/2025 12:00"); // hard code ngay` 1/6/2025 00:00:00 GMT+7

            modelBuilder.Entity<User>().HasData(
                new User { Id = 1, Username = "admin", Password = HashHelper.Hash("123456"), Role = "admin", Email = "admin@brainbox.com", Status = true, Avatar = "", CreatedAt = now, PremiumExpiredAt = 0 },
                new User { Id = 2, Username = "teacher1", Password = HashHelper.Hash("123456"), Role = "teacher", Email = "teacher1@brainbox.com", Status = true, Avatar = "", CreatedAt = now, PremiumExpiredAt = 0 },
                new User { Id = 3, Username = "teacher2", Password = HashHelper.Hash("123456"), Role = "teacher", Email = "teacher2@brainbox.com", Status = true, Avatar = "", CreatedAt = now, PremiumExpiredAt = 0 },
                new User { Id = 4, Username = "user1", Password = HashHelper.Hash("123456"), Role = "user", Email = "user1@brainbox.com", Status = true, Avatar = "", CreatedAt = now, PremiumExpiredAt = 0 },
                new User { Id = 5, Username = "user2", Password = HashHelper.Hash("123456"), Role = "user", Email = "user2@brainbox.com", Status = true, Avatar = "", CreatedAt = now, PremiumExpiredAt = 0 }
            );

            modelBuilder.Entity<Quiz>().HasData(
                new Quiz { QuizId = 1, QuizName = "Basic Math", Description = "Simple arithmetic quiz", CreatorId = 2, IsPublic = true, CreatedAt = now },
                new Quiz { QuizId = 2, QuizName = "English Vocabulary", Description = "Common English words", CreatorId = 3, IsPublic = true, CreatedAt = now },
                new Quiz { QuizId = 3, QuizName = "Địa lý thế giới", Description = "Câu hỏi về quốc gia, thủ đô và danh lam thắng cảnh", CreatorId = 2, IsPublic = true, CreatedAt = now },
                new Quiz { QuizId = 4, QuizName = "Vật lý cơ bản", Description = "Kiến thức nền tảng về cơ học và nhiệt học", CreatorId = 4, IsPublic = true, CreatedAt = now },
                new Quiz { QuizId = 5, QuizName = "Lịch sử Việt Nam", Description = "Các sự kiện và nhân vật lịch sử nổi bật", CreatorId = 3, IsPublic = true, CreatedAt = now },
                new Quiz { QuizId = 6, QuizName = "Sinh học nhập môn", Description = "Câu hỏi về tế bào, di truyền và hệ sinh thái", CreatorId = 5, IsPublic = true, CreatedAt = now },
                new Quiz { QuizId = 7, QuizName = "Luyện đọc IELTS", Description = "Bài đọc kèm câu hỏi theo chuẩn IELTS", CreatorId = 2, IsPublic = true, CreatedAt = now },
                new Quiz { QuizId = 8, QuizName = "Thuật ngữ tin học", Description = "Thuật ngữ và khái niệm cơ bản trong CNTT", CreatorId = 4, IsPublic = true, CreatedAt = now },
                new Quiz { QuizId = 9, QuizName = "Hiragana tiếng Nhật", Description = "Nhận diện chữ cái Hiragana cơ bản", CreatorId = 3, IsPublic = false, CreatedAt = now },
                new Quiz { QuizId = 10, QuizName = "Tư duy logic", Description = "Câu hỏi rèn luyện khả năng suy luận logic", CreatorId = 5, IsPublic = false, CreatedAt = now }
            );
            modelBuilder.Entity<Flashcard>().HasData(
                new Flashcard { CardId = 1, QuizId = 1, Question = "1 + 1 = ?", Option1 = "1", Option2 = "2", Option3 = "3", Option4 = "4", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 2, QuizId = 1, Question = "5 - 3 = ?", Option1 = "3", Option2 = "2", Option3 = "1", Option4 = "0", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 3, QuizId = 1, Question = "4 × 2 = ?", Option1 = "6", Option2 = "8", Option3 = "10", Option4 = "12", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 4, QuizId = 1, Question = "9 ÷ 3 = ?", Option1 = "1", Option2 = "2", Option3 = "3", Option4 = "4", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 5, QuizId = 1, Question = "2² = ?", Option1 = "2", Option2 = "4", Option3 = "6", Option4 = "8", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 6, QuizId = 1, Question = "√16 = ?", Option1 = "3", Option2 = "4", Option3 = "5", Option4 = "6", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 7, QuizId = 1, Question = "10 % 3 = ?", Option1 = "0", Option2 = "1", Option3 = "2", Option4 = "3", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 8, QuizId = 1, Question = "3 + 7 × 2 = ?", Option1 = "17", Option2 = "20", Option3 = "23", Option4 = "24", Answer = 1, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 9, QuizId = 1, Question = "15 ÷ (3 × 1) = ?", Option1 = "4", Option2 = "5", Option3 = "6", Option4 = "7", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 10, QuizId = 1, Question = "7 × 7 = ?", Option1 = "42", Option2 = "48", Option3 = "49", Option4 = "56", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 11, QuizId = 1, Question = "12 + 8 = ?", Option1 = "18", Option2 = "20", Option3 = "22", Option4 = "24", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 12, QuizId = 1, Question = "100 - 45 = ?", Option1 = "45", Option2 = "50", Option3 = "55", Option4 = "60", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 13, QuizId = 1, Question = "3³ = ?", Option1 = "6", Option2 = "9", Option3 = "27", Option4 = "81", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 14, QuizId = 1, Question = "0 × 1000 = ?", Option1 = "0", Option2 = "1000", Option3 = "1", Option4 = "∞", Answer = 1, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 15, QuizId = 1, Question = "What is the value of π (approx)?", Option1 = "3.12", Option2 = "3.14", Option3 = "3.16", Option4 = "3.18", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 16, QuizId = 1, Question = "6 × 8 = ?", Option1 = "42", Option2 = "46", Option3 = "48", Option4 = "54", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 17, QuizId = 1, Question = "50 ÷ 5 = ?", Option1 = "10", Option2 = "15", Option3 = "20", Option4 = "25", Answer = 1, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 18, QuizId = 1, Question = "2 + 2 × 2 = ?", Option1 = "6", Option2 = "8", Option3 = "4", Option4 = "2", Answer = 1, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 19, QuizId = 1, Question = "25% of 80 = ?", Option1 = "15", Option2 = "20", Option3 = "25", Option4 = "30", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 20, QuizId = 1, Question = "Area of square with side 4?", Option1 = "8", Option2 = "12", Option3 = "16", Option4 = "20", Answer = 3, CreatorId = 3, CreatedAt = now },
            
                new Flashcard { CardId = 21, QuizId = 2, Question = "Từ đồng nghĩa của 'happy' là gì?", Option1 = "sad", Option2 = "joyful", Option3 = "angry", Option4 = "tired", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 22, QuizId = 2, Question = "'Rapid' nghĩa là gì?", Option1 = "slow", Option2 = "confused", Option3 = "fast", Option4 = "hard", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 23, QuizId = 2, Question = "Từ trái nghĩa với 'ancient' là gì?", Option1 = "modern", Option2 = "old", Option3 = "historic", Option4 = "antique", Answer = 1, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 24, QuizId = 2, Question = "Chọn từ có cách viết đúng:", Option1 = "accomodate", Option2 = "accommodate", Option3 = "acommodate", Option4 = "accomadate", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 25, QuizId = 2, Question = "'Benevolent' gần nghĩa nhất với từ nào?", Option1 = "kind", Option2 = "strong", Option3 = "fast", Option4 = "silent", Answer = 1, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 26, QuizId = 2, Question = "'Reluctant' nghĩa là gì?", Option1 = "willing", Option2 = "eager", Option3 = "hesitant", Option4 = "excited", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 27, QuizId = 2, Question = "Fill in the blank: He is ___ to help.", Option1 = "reluctant", Option2 = "aggressive", Option3 = "lazy", Option4 = "honest", Answer = 1, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 28, QuizId = 2, Question = "'Fragile' means:", Option1 = "strong", Option2 = "breakable", Option3 = "heavy", Option4 = "sharp", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 29, QuizId = 2, Question = "The word 'abundant' means:", Option1 = "scarce", Option2 = "plentiful", Option3 = "dangerous", Option4 = "small", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 30, QuizId = 2, Question = "Which is a noun?", Option1 = "run", Option2 = "happy", Option3 = "happiness", Option4 = "quick", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 31, QuizId = 2, Question = "Choose the adjective:", Option1 = "beauty", Option2 = "beautiful", Option3 = "beautifully", Option4 = "beautify", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 32, QuizId = 2, Question = "'Generous' is a person who:", Option1 = "keeps everything", Option2 = "shares a lot", Option3 = "talks a lot", Option4 = "eats a lot", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 33, QuizId = 2, Question = "Opposite of 'optimistic'?", Option1 = "hopeful", Option2 = "cheerful", Option3 = "pessimistic", Option4 = "positive", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 34, QuizId = 2, Question = "'Enormous' means:", Option1 = "tiny", Option2 = "huge", Option3 = "normal", Option4 = "average", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 35, QuizId = 2, Question = "'He is an ___ speaker.' Fill the blank", Option1 = "effect", Option2 = "effective", Option3 = "affect", Option4 = "effectively", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 36, QuizId = 2, Question = "'Brief' means:", Option1 = "long", Option2 = "short", Option3 = "deep", Option4 = "boring", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 37, QuizId = 2, Question = "What is the plural of 'analysis'?", Option1 = "analysises", Option2 = "analys", Option3 = "analyses", Option4 = "analysises", Answer = 3, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 38, QuizId = 2, Question = "Choose the correct word: He ___ every weekend.", Option1 = "work", Option2 = "works", Option3 = "worked", Option4 = "working", Answer = 2, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 39, QuizId = 2, Question = "'Cautious' means:", Option1 = "careful", Option2 = "brave", Option3 = "fast", Option4 = "silent", Answer = 1, CreatorId = 3, CreatedAt = now },
                new Flashcard { CardId = 40, QuizId = 2, Question = "Which word means 'completely necessary'?", Option1 = "optional", Option2 = "rare", Option3 = "essential", Option4 = "easy", Answer = 3, CreatorId = 3, CreatedAt = now }
                
            );
            modelBuilder.Entity<Document>().HasData(
                new Document { DocId = 1, Title = "Android cơ bản", Content = "trong docs này chúng tôi dạy bạn android cơ bản nè", AuthorId = 1, IsPublic = true, Views = 10, CreatedAt = now },
                new Document { DocId = 2, Title = "ASP.NET core API", Content = "trong doc này chúng tôi hướng dẫn bạn về c# API", AuthorId = 2, IsPublic = true, Views = 5, CreatedAt = now },
                new Document { DocId = 3, Title = "Sinh học tế bào", Content = "Tài liệu cung cấp kiến thức cơ bản về cấu trúc và chức năng của tế bào.", AuthorId = 1, IsPublic = true, Views = 32, CreatedAt = now },
                new Document { DocId = 4, Title = "Văn học dân gian Việt Nam", Content = "Tổng hợp truyện cổ tích, ca dao, tục ngữ đặc sắc trong kho tàng dân gian.", AuthorId = 3, IsPublic = true, Views = 21, CreatedAt = now },
                new Document { DocId = 5, Title = "Địa lý tự nhiên Việt Nam", Content = "Trình bày khí hậu, địa hình, sông ngòi và vùng miền trên lãnh thổ Việt Nam.", AuthorId = 4, IsPublic = true, Views = 14, CreatedAt = now },
                new Document { DocId = 6, Title = "Hệ mặt trời", Content = "Tìm hiểu về các hành tinh, vệ tinh và hiện tượng thiên văn cơ bản.", AuthorId = 2, IsPublic = true, Views = 19, CreatedAt = now },
                new Document { DocId = 7, Title = "Các cuộc cách mạng tư sản", Content = "Phân tích các cuộc cách mạng lớn: Anh, Mỹ, Pháp và ảnh hưởng của chúng.", AuthorId = 1, IsPublic = true, Views = 16, CreatedAt = now },
                new Document { DocId = 8, Title = "Thực vật học", Content = "Khám phá cấu trúc, phân loại và vai trò của thực vật trong tự nhiên.", AuthorId = 3, IsPublic = true, Views = 20, CreatedAt = now },
                new Document { DocId = 9, Title = "Tâm lý học giáo dục", Content = "Tài liệu nội bộ nghiên cứu tâm lý học sinh trong quá trình học tập.", AuthorId = 4, IsPublic = false, Views = 3, CreatedAt = now },
                new Document { DocId = 10, Title = "Kinh tế học vi mô", Content = "Tài liệu chuyên sâu về hành vi người tiêu dùng và doanh nghiệp trong thị trường.", AuthorId = 2, IsPublic = false, Views = 2, CreatedAt = now },
                new Document { DocId = 11, Title = "", Content = "Tài liệu chuyên sâu về hành vi người tiêu dùng và doanh nghiệp trong thị trường.", AuthorId = 2, IsPublic = false, Views = 2, CreatedAt = now }
            );

            modelBuilder.Entity<DocumentDetail>().HasData(
                new DocumentDetail { DocDetailId = 1, DocId = 1, ImageUrl = "https://developer.android.com/static/studio/images/write/layout-editor.png?hl=vi", Caption = "Giao diện Android Studio", CreatedAt = now },
                new DocumentDetail { DocDetailId = 2, DocId = 1, ImageUrl = "https://images.unsplash.com/photo-1580894894513-1a28cbb71ec4", Caption = "XML Layout", CreatedAt = now },
                new DocumentDetail { DocDetailId = 3, DocId = 1, ImageUrl = "https://developer.android.com/images/training/basics/firstapp/first-app-studio.png", Caption = "Tạo project đầu tiên", CreatedAt = now },
                new DocumentDetail { DocDetailId = 4, DocId = 1, ImageUrl = "https://images.unsplash.com/photo-1531482615713-2afd69097998", Caption = "Activity Lifecycle", CreatedAt = now },
                new DocumentDetail { DocDetailId = 5, DocId = 1, ImageUrl = "https://developer.android.com/images/training/basics/firstapp/hello-world.png", Caption = "Kết quả Hello World", CreatedAt = now },
                new DocumentDetail { DocDetailId = 6, DocId = 1, ImageUrl = "https://images.unsplash.com/photo-1580894746646-4af2bcfe7070", Caption = "Code Java cơ bản", CreatedAt = now },
                new DocumentDetail { DocDetailId = 7, DocId = 1, ImageUrl = "https://developer.android.com/images/training/basics/firstapp/emulator.png", Caption = "Android Emulator", CreatedAt = now },
                new DocumentDetail { DocDetailId = 8, DocId = 1, ImageUrl = "https://images.unsplash.com/photo-1517336714731-489689fd1ca8", Caption = "Thiết kế UI", CreatedAt = now },
                new DocumentDetail { DocDetailId = 9, DocId = 1, ImageUrl = "https://developer.android.com/images/training/basics/firstapp/project-structure.png", Caption = "Cấu trúc project", CreatedAt = now },
                new DocumentDetail { DocDetailId = 10, DocId = 1, ImageUrl = "https://developer.android.com/images/training/basics/firstapp/logcat.png", Caption = "Logcat Output", CreatedAt = now },

                new DocumentDetail { DocDetailId = 11, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/help-page-api.png", Caption = "Swagger UI", CreatedAt = now },
                new DocumentDetail { DocDetailId = 12, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/add-controller.png", Caption = "Tạo Controller", CreatedAt = now },
                new DocumentDetail { DocDetailId = 13, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/configure-services.png", Caption = "ConfigureServices()", CreatedAt = now },
                new DocumentDetail { DocDetailId = 14, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/jwt-auth.png", Caption = "Xác thực JWT", CreatedAt = now },
                new DocumentDetail { DocDetailId = 15, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/controller-code.png", Caption = "Controller code", CreatedAt = now },
                new DocumentDetail { DocDetailId = 16, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/database-diagram.png", Caption = "Sơ đồ Database", CreatedAt = now },
                new DocumentDetail { DocDetailId = 17, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/http-request.png", Caption = "Request mẫu", CreatedAt = now },
                new DocumentDetail { DocDetailId = 18, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/appsettings.png", Caption = "Cấu hình JWT", CreatedAt = now },
                new DocumentDetail { DocDetailId = 19, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/entity-model.png", Caption = "Entity Model", CreatedAt = now },
                new DocumentDetail { DocDetailId = 20, DocId = 2, ImageUrl = "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/database-connection.png", Caption = "Kết nối DB", CreatedAt = now }
            );


            modelBuilder.Entity<Tag>().HasData(
                new Tag { TagId = 1, TagName = "English", Description = "Học tiếng anh" },
                new Tag { TagId = 2, TagName = ".Net", Description = "học C# .Net" },
                new Tag { TagId = 3, TagName = "Android", Description = "học viết ứng dụng android" },
                new Tag { TagId = 4, TagName = "Code", Description = "học làm thợ Code" },
                new Tag { TagId = 5, TagName = "Vocabulary", Description = "học từ vựng" },
                new Tag { TagId = 6, TagName = "Tự nhiên", Description = "Khoa học tự nhiên" },
                new Tag { TagId = 7, TagName = "Xã hội", Description = "Khoa học xã hội" },
                new Tag { TagId = 8, TagName = "Kinh tế", Description = "kinh tế học vĩ mô, kinh tế học vi mô" },
                new Tag { TagId = 9, TagName = "Vật lý", Description = "Vật lý học tổng quát" },
                new Tag { TagId = 10, TagName = "Xuyên không học", Description = "Cơ học lượng tử, lý thuyết xuyên qua không thời gian" }
            );

            modelBuilder.Entity<DocumentTagCrossRef>().HasData(
                new DocumentTagCrossRef { DocId = 1, TagId = 3 },
                new DocumentTagCrossRef { DocId = 1, TagId = 4 },
                new DocumentTagCrossRef { DocId = 2, TagId = 2 },
                new DocumentTagCrossRef { DocId = 2, TagId = 4 },
                new DocumentTagCrossRef { DocId = 3, TagId = 6 },
                new DocumentTagCrossRef { DocId = 3, TagId = 10 },
                new DocumentTagCrossRef { DocId = 4, TagId = 7 },
                new DocumentTagCrossRef { DocId = 5, TagId = 6 },
                new DocumentTagCrossRef { DocId = 6, TagId = 6 },
                new DocumentTagCrossRef { DocId = 7, TagId = 7 },
                new DocumentTagCrossRef { DocId = 8, TagId = 6 },
                new DocumentTagCrossRef { DocId = 9, TagId = 7 },
                new DocumentTagCrossRef { DocId = 10, TagId = 8 }
            );

            modelBuilder.Entity<RatingQuiz>().HasData(
                new RatingQuiz { RatingId = 1, QuizId = 1, UserId = 1, Rating = 5, Comment = "Rất dễ hiểu và trực quan!", RatedAt = now },
                new RatingQuiz { RatingId = 2, QuizId = 1, UserId = 2, Rating = 4, Comment = "Tốt nhưng có thể thêm câu hỏi nâng cao.", RatedAt = now },
                new RatingQuiz { RatingId = 3, QuizId = 1, UserId = 3, Rating = 3, Comment = "Câu hỏi hơi đơn giản.", RatedAt = now },
                new RatingQuiz { RatingId = 4, QuizId = 1, UserId = 4, Rating = 5, Comment = "Hoàn hảo cho người mới học toán.", RatedAt = now },
                new RatingQuiz { RatingId = 5, QuizId = 1, UserId = 5, Rating = 4, Comment = "Giao diện tốt, dễ thao tác.", RatedAt = now },

                new RatingQuiz { RatingId = 6, QuizId = 2, UserId = 1, Rating = 4, Comment = "Từ vựng phong phú, hữu ích.", RatedAt = now },
                new RatingQuiz { RatingId = 7, QuizId = 2, UserId = 2, Rating = 5, Comment = "Rất thích hợp ôn luyện IELTS.", RatedAt = now },
                new RatingQuiz { RatingId = 8, QuizId = 2, UserId = 3, Rating = 3, Comment = "Thiếu phần giải thích nghĩa từ.", RatedAt = now },
                new RatingQuiz { RatingId = 9, QuizId = 2, UserId = 4, Rating = 4, Comment = "Chất lượng tốt, đề phong phú.", RatedAt = now },
                new RatingQuiz { RatingId = 10, QuizId = 2, UserId = 5, Rating = 5, Comment = "Giúp mình mở rộng vốn từ rất nhiều.", RatedAt = now }
);


            modelBuilder.Entity<Bookmark>().HasData(
                new Bookmark { UserId = 2, QuizId = 1, LastCardId = 1, BookmarkedAt = now }
            );

            modelBuilder.Entity<DownloadHistory>().HasData(
                new DownloadHistory { UserId = 3, TargetId = 1, TargetType= "document", DownloadedAt = now }
            );

            modelBuilder.Entity<Comment>().HasData(
                new Comment { CommentId = 1, DocDetailId = 1, UserId = 2, Content = "Học android rất là hay luôn", CreatedAt = now },
                new Comment { CommentId = 2, DocDetailId = 1, UserId = 4, Content = "đỡ tốn tiền học trung tâm", CreatedAt = now },
                new Comment { CommentId = 3, DocDetailId = 1, UserId = 5, Content = "Ảnh bị sao vậy sao không hiển thị", CreatedAt = now },
                new Comment { CommentId = 4, DocDetailId = 2, UserId = 3, Content = "có 20 trang thôi à, thêm đi", CreatedAt = now }
            );

            modelBuilder.Entity<Challenge>().HasData(
                new Challenge { ChallengeId = 1, QuizId = 1, ChallengerId = 1, OpponentId = 2, Status = 0, ChallengerScore = 0, OpponentScore = 0, CreatedAt = now }
            );

            modelBuilder.Entity<Notification>().HasData(
                new Notification { NotificationId = 1, UserId = 2, Content = "You were challenged!", Type = "challenge", RelatedId = 1, IsRead = false, ReadAt = 0, CreatedAt = now }
            );
        }

    }
}
