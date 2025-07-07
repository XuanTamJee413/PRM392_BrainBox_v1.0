using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace BrainBoxAPI.Migrations
{
    /// <inheritdoc />
    public partial class create : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Tags",
                columns: table => new
                {
                    TagId = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    TagName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Description = table.Column<string>(type: "nvarchar(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Tags", x => x.TagId);
                });

            migrationBuilder.CreateTable(
                name: "Users",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Username = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Password = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Role = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Email = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Status = table.Column<bool>(type: "bit", nullable: false),
                    Avatar = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    CreatedAt = table.Column<long>(type: "bigint", nullable: false),
                    PremiumExpiredAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Users", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Documents",
                columns: table => new
                {
                    DocId = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Title = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Content = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    AuthorId = table.Column<int>(type: "int", nullable: false),
                    IsPublic = table.Column<bool>(type: "bit", nullable: false),
                    Views = table.Column<int>(type: "int", nullable: false),
                    CreatedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Documents", x => x.DocId);
                    table.ForeignKey(
                        name: "FK_Documents_Users_AuthorId",
                        column: x => x.AuthorId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "DownloadHistories",
                columns: table => new
                {
                    UserId = table.Column<int>(type: "int", nullable: false),
                    TargetId = table.Column<int>(type: "int", nullable: false),
                    TargetType = table.Column<string>(type: "nvarchar(450)", nullable: false),
                    DownloadedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DownloadHistories", x => new { x.UserId, x.TargetId, x.TargetType });
                    table.ForeignKey(
                        name: "FK_DownloadHistories_Users_UserId",
                        column: x => x.UserId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "Notifications",
                columns: table => new
                {
                    NotificationId = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    UserId = table.Column<int>(type: "int", nullable: false),
                    Content = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Type = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    RelatedId = table.Column<int>(type: "int", nullable: false),
                    IsRead = table.Column<bool>(type: "bit", nullable: false),
                    ReadAt = table.Column<long>(type: "bigint", nullable: false),
                    CreatedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Notifications", x => x.NotificationId);
                    table.ForeignKey(
                        name: "FK_Notifications_Users_UserId",
                        column: x => x.UserId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "Quizzes",
                columns: table => new
                {
                    QuizId = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    QuizName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Description = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    CreatorId = table.Column<int>(type: "int", nullable: false),
                    IsPublic = table.Column<bool>(type: "bit", nullable: false),
                    CreatedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Quizzes", x => x.QuizId);
                    table.ForeignKey(
                        name: "FK_Quizzes_Users_CreatorId",
                        column: x => x.CreatorId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "DocumentDetails",
                columns: table => new
                {
                    DocDetailId = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    DocId = table.Column<int>(type: "int", nullable: false),
                    ImageUrl = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Caption = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    CreatedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DocumentDetails", x => x.DocDetailId);
                    table.ForeignKey(
                        name: "FK_DocumentDetails_Documents_DocId",
                        column: x => x.DocId,
                        principalTable: "Documents",
                        principalColumn: "DocId",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "DocumentTagCrossRefs",
                columns: table => new
                {
                    DocId = table.Column<int>(type: "int", nullable: false),
                    TagId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DocumentTagCrossRefs", x => new { x.DocId, x.TagId });
                    table.ForeignKey(
                        name: "FK_DocumentTagCrossRefs_Documents_DocId",
                        column: x => x.DocId,
                        principalTable: "Documents",
                        principalColumn: "DocId",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_DocumentTagCrossRefs_Tags_TagId",
                        column: x => x.TagId,
                        principalTable: "Tags",
                        principalColumn: "TagId",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Challenges",
                columns: table => new
                {
                    ChallengeId = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    QuizId = table.Column<int>(type: "int", nullable: false),
                    ChallengerId = table.Column<int>(type: "int", nullable: false),
                    OpponentId = table.Column<int>(type: "int", nullable: false),
                    Status = table.Column<int>(type: "int", nullable: false),
                    ChallengerScore = table.Column<int>(type: "int", nullable: false),
                    OpponentScore = table.Column<int>(type: "int", nullable: false),
                    CreatedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Challenges", x => x.ChallengeId);
                    table.ForeignKey(
                        name: "FK_Challenges_Quizzes_QuizId",
                        column: x => x.QuizId,
                        principalTable: "Quizzes",
                        principalColumn: "QuizId",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Challenges_Users_ChallengerId",
                        column: x => x.ChallengerId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_Challenges_Users_OpponentId",
                        column: x => x.OpponentId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "Flashcards",
                columns: table => new
                {
                    CardId = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    QuizId = table.Column<int>(type: "int", nullable: false),
                    Question = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Option1 = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Option2 = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Option3 = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Option4 = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Answer = table.Column<int>(type: "int", nullable: false),
                    CreatorId = table.Column<int>(type: "int", nullable: false),
                    CreatedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Flashcards", x => x.CardId);
                    table.ForeignKey(
                        name: "FK_Flashcards_Quizzes_QuizId",
                        column: x => x.QuizId,
                        principalTable: "Quizzes",
                        principalColumn: "QuizId",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Flashcards_Users_CreatorId",
                        column: x => x.CreatorId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "RatingQuizzes",
                columns: table => new
                {
                    RatingId = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    QuizId = table.Column<int>(type: "int", nullable: false),
                    UserId = table.Column<int>(type: "int", nullable: false),
                    Rating = table.Column<int>(type: "int", nullable: false),
                    Comment = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    RatedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_RatingQuizzes", x => x.RatingId);
                    table.ForeignKey(
                        name: "FK_RatingQuizzes_Quizzes_QuizId",
                        column: x => x.QuizId,
                        principalTable: "Quizzes",
                        principalColumn: "QuizId",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_RatingQuizzes_Users_UserId",
                        column: x => x.UserId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "Comments",
                columns: table => new
                {
                    CommentId = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    DocDetailId = table.Column<int>(type: "int", nullable: false),
                    UserId = table.Column<int>(type: "int", nullable: false),
                    Content = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    CreatedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Comments", x => x.CommentId);
                    table.ForeignKey(
                        name: "FK_Comments_DocumentDetails_DocDetailId",
                        column: x => x.DocDetailId,
                        principalTable: "DocumentDetails",
                        principalColumn: "DocDetailId",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Comments_Users_UserId",
                        column: x => x.UserId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "Bookmarks",
                columns: table => new
                {
                    UserId = table.Column<int>(type: "int", nullable: false),
                    QuizId = table.Column<int>(type: "int", nullable: false),
                    LastCardId = table.Column<int>(type: "int", nullable: true),
                    BookmarkedAt = table.Column<long>(type: "bigint", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Bookmarks", x => new { x.UserId, x.QuizId });
                    table.ForeignKey(
                        name: "FK_Bookmarks_Flashcards_LastCardId",
                        column: x => x.LastCardId,
                        principalTable: "Flashcards",
                        principalColumn: "CardId",
                        onDelete: ReferentialAction.SetNull);
                    table.ForeignKey(
                        name: "FK_Bookmarks_Quizzes_QuizId",
                        column: x => x.QuizId,
                        principalTable: "Quizzes",
                        principalColumn: "QuizId",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_Bookmarks_Users_UserId",
                        column: x => x.UserId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.InsertData(
                table: "Tags",
                columns: new[] { "TagId", "Description", "TagName" },
                values: new object[,]
                {
                    { 1, "Học tiếng anh", "English" },
                    { 2, "học C# .Net", ".Net" },
                    { 3, "học viết ứng dụng android", "Android" },
                    { 4, "học làm thợ Code", "Code" },
                    { 5, "học từ vựng", "Vocabulary" }
                });

            migrationBuilder.InsertData(
                table: "Users",
                columns: new[] { "Id", "Avatar", "CreatedAt", "Email", "Password", "PremiumExpiredAt", "Role", "Status", "Username" },
                values: new object[,]
                {
                    { 1, "", 1743752400000L, "admin@brainbox.com", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 0L, "admin", true, "admin" },
                    { 2, "", 1743752400000L, "teacher1@brainbox.com", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 0L, "teacher", true, "teacher1" },
                    { 3, "", 1743752400000L, "teacher2@brainbox.com", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 0L, "teacher", true, "teacher2" },
                    { 4, "", 1743752400000L, "user1@brainbox.com", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 0L, "user", true, "user1" },
                    { 5, "", 1743752400000L, "user2@brainbox.com", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 0L, "user", true, "user2" }
                });

            migrationBuilder.InsertData(
                table: "Documents",
                columns: new[] { "DocId", "AuthorId", "Content", "CreatedAt", "IsPublic", "Title", "Views" },
                values: new object[,]
                {
                    { 1, 1, "trong docs này chúng tôi dạy bạn android cơ bản nè", 1743752400000L, true, "Android cơ bản", 10 },
                    { 2, 2, "trong doc này chúng tôi hướng dẫn bạn về c# API", 1743752400000L, false, "ASP.NET core API", 5 }
                });

            migrationBuilder.InsertData(
                table: "DownloadHistories",
                columns: new[] { "TargetId", "TargetType", "UserId", "DownloadedAt" },
                values: new object[] { 1, "document", 3, 1743752400000L });

            migrationBuilder.InsertData(
                table: "Notifications",
                columns: new[] { "NotificationId", "Content", "CreatedAt", "IsRead", "ReadAt", "RelatedId", "Type", "UserId" },
                values: new object[] { 1, "You were challenged!", 1743752400000L, false, 0L, 1, "challenge", 2 });

            migrationBuilder.InsertData(
                table: "Quizzes",
                columns: new[] { "QuizId", "CreatedAt", "CreatorId", "Description", "IsPublic", "QuizName" },
                values: new object[,]
                {
                    { 1, 1743752400000L, 2, "Simple arithmetic quiz", true, "Basic Math" },
                    { 2, 1743752400000L, 3, "Common English words", true, "English Vocabulary" }
                });

            migrationBuilder.InsertData(
                table: "Challenges",
                columns: new[] { "ChallengeId", "ChallengerId", "ChallengerScore", "CreatedAt", "OpponentId", "OpponentScore", "QuizId", "Status" },
                values: new object[] { 1, 1, 0, 1743752400000L, 2, 0, 1, 0 });

            migrationBuilder.InsertData(
                table: "DocumentDetails",
                columns: new[] { "DocDetailId", "Caption", "CreatedAt", "DocId", "ImageUrl" },
                values: new object[,]
                {
                    { 1, "Giao diện Android Studio", 1743752400000L, 1, "https://developer.android.com/static/studio/images/write/layout-editor.png?hl=vi" },
                    { 2, "XML Layout", 1743752400000L, 1, "https://images.unsplash.com/photo-1580894894513-1a28cbb71ec4" },
                    { 3, "Tạo project đầu tiên", 1743752400000L, 1, "https://developer.android.com/images/training/basics/firstapp/first-app-studio.png" },
                    { 4, "Activity Lifecycle", 1743752400000L, 1, "https://images.unsplash.com/photo-1531482615713-2afd69097998" },
                    { 5, "Kết quả Hello World", 1743752400000L, 1, "https://developer.android.com/images/training/basics/firstapp/hello-world.png" },
                    { 6, "Code Java cơ bản", 1743752400000L, 1, "https://images.unsplash.com/photo-1580894746646-4af2bcfe7070" },
                    { 7, "Android Emulator", 1743752400000L, 1, "https://developer.android.com/images/training/basics/firstapp/emulator.png" },
                    { 8, "Thiết kế UI", 1743752400000L, 1, "https://images.unsplash.com/photo-1517336714731-489689fd1ca8" },
                    { 9, "Cấu trúc project", 1743752400000L, 1, "https://developer.android.com/images/training/basics/firstapp/project-structure.png" },
                    { 10, "Logcat Output", 1743752400000L, 1, "https://developer.android.com/images/training/basics/firstapp/logcat.png" },
                    { 11, "Swagger UI", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/help-page-api.png" },
                    { 12, "Tạo Controller", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/add-controller.png" },
                    { 13, "ConfigureServices()", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/configure-services.png" },
                    { 14, "Xác thực JWT", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/jwt-auth.png" },
                    { 15, "Controller code", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/controller-code.png" },
                    { 16, "Sơ đồ Database", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/database-diagram.png" },
                    { 17, "Request mẫu", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/http-request.png" },
                    { 18, "Cấu hình JWT", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/appsettings.png" },
                    { 19, "Entity Model", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/entity-model.png" },
                    { 20, "Kết nối DB", 1743752400000L, 2, "https://learn.microsoft.com/en-us/aspnet/core/tutorials/web-api-help-page/_static/database-connection.png" }
                });

            migrationBuilder.InsertData(
                table: "DocumentTagCrossRefs",
                columns: new[] { "DocId", "TagId" },
                values: new object[,]
                {
                    { 1, 3 },
                    { 1, 4 },
                    { 2, 2 },
                    { 2, 4 }
                });

            migrationBuilder.InsertData(
                table: "Flashcards",
                columns: new[] { "CardId", "Answer", "CreatedAt", "CreatorId", "Option1", "Option2", "Option3", "Option4", "Question", "QuizId" },
                values: new object[,]
                {
                    { 1, 2, 1743752400000L, 3, "1", "2", "3", "4", "1 + 1 = ?", 1 },
                    { 2, 2, 1743752400000L, 3, "3", "2", "1", "0", "5 - 3 = ?", 1 },
                    { 3, 2, 1743752400000L, 3, "6", "8", "10", "12", "4 × 2 = ?", 1 },
                    { 4, 3, 1743752400000L, 3, "1", "2", "3", "4", "9 ÷ 3 = ?", 1 },
                    { 5, 2, 1743752400000L, 3, "2", "4", "6", "8", "2² = ?", 1 },
                    { 6, 2, 1743752400000L, 3, "3", "4", "5", "6", "√16 = ?", 1 },
                    { 7, 3, 1743752400000L, 3, "0", "1", "2", "3", "10 % 3 = ?", 1 },
                    { 8, 1, 1743752400000L, 3, "17", "20", "23", "24", "3 + 7 × 2 = ?", 1 },
                    { 9, 2, 1743752400000L, 3, "4", "5", "6", "7", "15 ÷ (3 × 1) = ?", 1 },
                    { 10, 3, 1743752400000L, 3, "42", "48", "49", "56", "7 × 7 = ?", 1 },
                    { 11, 2, 1743752400000L, 3, "18", "20", "22", "24", "12 + 8 = ?", 1 },
                    { 12, 3, 1743752400000L, 3, "45", "50", "55", "60", "100 - 45 = ?", 1 },
                    { 13, 3, 1743752400000L, 3, "6", "9", "27", "81", "3³ = ?", 1 },
                    { 14, 1, 1743752400000L, 3, "0", "1000", "1", "∞", "0 × 1000 = ?", 1 },
                    { 15, 2, 1743752400000L, 3, "3.12", "3.14", "3.16", "3.18", "What is the value of π (approx)?", 1 },
                    { 16, 3, 1743752400000L, 3, "42", "46", "48", "54", "6 × 8 = ?", 1 },
                    { 17, 1, 1743752400000L, 3, "10", "15", "20", "25", "50 ÷ 5 = ?", 1 },
                    { 18, 1, 1743752400000L, 3, "6", "8", "4", "2", "2 + 2 × 2 = ?", 1 },
                    { 19, 2, 1743752400000L, 3, "15", "20", "25", "30", "25% of 80 = ?", 1 },
                    { 20, 3, 1743752400000L, 3, "8", "12", "16", "20", "Area of square with side 4?", 1 },
                    { 21, 2, 1743752400000L, 3, "sad", "joyful", "angry", "tired", "Từ đồng nghĩa của 'happy' là gì?", 2 },
                    { 22, 3, 1743752400000L, 3, "slow", "confused", "fast", "hard", "'Rapid' nghĩa là gì?", 2 },
                    { 23, 1, 1743752400000L, 3, "modern", "old", "historic", "antique", "Từ trái nghĩa với 'ancient' là gì?", 2 },
                    { 24, 2, 1743752400000L, 3, "accomodate", "accommodate", "acommodate", "accomadate", "Chọn từ có cách viết đúng:", 2 },
                    { 25, 1, 1743752400000L, 3, "kind", "strong", "fast", "silent", "'Benevolent' gần nghĩa nhất với từ nào?", 2 },
                    { 26, 3, 1743752400000L, 3, "willing", "eager", "hesitant", "excited", "'Reluctant' nghĩa là gì?", 2 },
                    { 27, 1, 1743752400000L, 3, "reluctant", "aggressive", "lazy", "honest", "Fill in the blank: He is ___ to help.", 2 },
                    { 28, 2, 1743752400000L, 3, "strong", "breakable", "heavy", "sharp", "'Fragile' means:", 2 },
                    { 29, 2, 1743752400000L, 3, "scarce", "plentiful", "dangerous", "small", "The word 'abundant' means:", 2 },
                    { 30, 3, 1743752400000L, 3, "run", "happy", "happiness", "quick", "Which is a noun?", 2 },
                    { 31, 2, 1743752400000L, 3, "beauty", "beautiful", "beautifully", "beautify", "Choose the adjective:", 2 },
                    { 32, 2, 1743752400000L, 3, "keeps everything", "shares a lot", "talks a lot", "eats a lot", "'Generous' is a person who:", 2 },
                    { 33, 3, 1743752400000L, 3, "hopeful", "cheerful", "pessimistic", "positive", "Opposite of 'optimistic'?", 2 },
                    { 34, 2, 1743752400000L, 3, "tiny", "huge", "normal", "average", "'Enormous' means:", 2 },
                    { 35, 2, 1743752400000L, 3, "effect", "effective", "affect", "effectively", "'He is an ___ speaker.' Fill the blank", 2 },
                    { 36, 2, 1743752400000L, 3, "long", "short", "deep", "boring", "'Brief' means:", 2 },
                    { 37, 3, 1743752400000L, 3, "analysises", "analys", "analyses", "analysises", "What is the plural of 'analysis'?", 2 },
                    { 38, 2, 1743752400000L, 3, "work", "works", "worked", "working", "Choose the correct word: He ___ every weekend.", 2 },
                    { 39, 1, 1743752400000L, 3, "careful", "brave", "fast", "silent", "'Cautious' means:", 2 },
                    { 40, 3, 1743752400000L, 3, "optional", "rare", "essential", "easy", "Which word means 'completely necessary'?", 2 }
                });

            migrationBuilder.InsertData(
                table: "RatingQuizzes",
                columns: new[] { "RatingId", "Comment", "QuizId", "RatedAt", "Rating", "UserId" },
                values: new object[,]
                {
                    { 1, "Rất dễ hiểu và trực quan!", 1, 1743752400000L, 5, 1 },
                    { 2, "Tốt nhưng có thể thêm câu hỏi nâng cao.", 1, 1743752400000L, 4, 2 },
                    { 3, "Câu hỏi hơi đơn giản.", 1, 1743752400000L, 3, 3 },
                    { 4, "Hoàn hảo cho người mới học toán.", 1, 1743752400000L, 5, 4 },
                    { 5, "Giao diện tốt, dễ thao tác.", 1, 1743752400000L, 4, 5 },
                    { 6, "Từ vựng phong phú, hữu ích.", 2, 1743752400000L, 4, 1 },
                    { 7, "Rất thích hợp ôn luyện IELTS.", 2, 1743752400000L, 5, 2 },
                    { 8, "Thiếu phần giải thích nghĩa từ.", 2, 1743752400000L, 3, 3 },
                    { 9, "Chất lượng tốt, đề phong phú.", 2, 1743752400000L, 4, 4 },
                    { 10, "Giúp mình mở rộng vốn từ rất nhiều.", 2, 1743752400000L, 5, 5 }
                });

            migrationBuilder.InsertData(
                table: "Bookmarks",
                columns: new[] { "QuizId", "UserId", "BookmarkedAt", "LastCardId" },
                values: new object[] { 1, 2, 1743752400000L, 1 });

            migrationBuilder.InsertData(
                table: "Comments",
                columns: new[] { "CommentId", "Content", "CreatedAt", "DocDetailId", "UserId" },
                values: new object[] { 1, "Nice image!", 1743752400000L, 1, 2 });

            migrationBuilder.CreateIndex(
                name: "IX_Bookmarks_LastCardId",
                table: "Bookmarks",
                column: "LastCardId");

            migrationBuilder.CreateIndex(
                name: "IX_Bookmarks_QuizId",
                table: "Bookmarks",
                column: "QuizId");

            migrationBuilder.CreateIndex(
                name: "IX_Challenges_ChallengerId",
                table: "Challenges",
                column: "ChallengerId");

            migrationBuilder.CreateIndex(
                name: "IX_Challenges_OpponentId",
                table: "Challenges",
                column: "OpponentId");

            migrationBuilder.CreateIndex(
                name: "IX_Challenges_QuizId",
                table: "Challenges",
                column: "QuizId");

            migrationBuilder.CreateIndex(
                name: "IX_Comments_DocDetailId",
                table: "Comments",
                column: "DocDetailId");

            migrationBuilder.CreateIndex(
                name: "IX_Comments_UserId",
                table: "Comments",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_DocumentDetails_DocId",
                table: "DocumentDetails",
                column: "DocId");

            migrationBuilder.CreateIndex(
                name: "IX_Documents_AuthorId",
                table: "Documents",
                column: "AuthorId");

            migrationBuilder.CreateIndex(
                name: "IX_DocumentTagCrossRefs_TagId",
                table: "DocumentTagCrossRefs",
                column: "TagId");

            migrationBuilder.CreateIndex(
                name: "IX_Flashcards_CreatorId",
                table: "Flashcards",
                column: "CreatorId");

            migrationBuilder.CreateIndex(
                name: "IX_Flashcards_QuizId",
                table: "Flashcards",
                column: "QuizId");

            migrationBuilder.CreateIndex(
                name: "IX_Notifications_UserId",
                table: "Notifications",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_Quizzes_CreatorId",
                table: "Quizzes",
                column: "CreatorId");

            migrationBuilder.CreateIndex(
                name: "IX_RatingQuizzes_QuizId",
                table: "RatingQuizzes",
                column: "QuizId");

            migrationBuilder.CreateIndex(
                name: "IX_RatingQuizzes_UserId",
                table: "RatingQuizzes",
                column: "UserId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Bookmarks");

            migrationBuilder.DropTable(
                name: "Challenges");

            migrationBuilder.DropTable(
                name: "Comments");

            migrationBuilder.DropTable(
                name: "DocumentTagCrossRefs");

            migrationBuilder.DropTable(
                name: "DownloadHistories");

            migrationBuilder.DropTable(
                name: "Notifications");

            migrationBuilder.DropTable(
                name: "RatingQuizzes");

            migrationBuilder.DropTable(
                name: "Flashcards");

            migrationBuilder.DropTable(
                name: "DocumentDetails");

            migrationBuilder.DropTable(
                name: "Tags");

            migrationBuilder.DropTable(
                name: "Quizzes");

            migrationBuilder.DropTable(
                name: "Documents");

            migrationBuilder.DropTable(
                name: "Users");
        }
    }
}
