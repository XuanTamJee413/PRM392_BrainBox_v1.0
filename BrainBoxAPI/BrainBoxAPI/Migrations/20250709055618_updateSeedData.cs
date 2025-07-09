using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace BrainBoxAPI.Migrations
{
    /// <inheritdoc />
    public partial class updateSeedData : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.UpdateData(
                table: "Bookmarks",
                keyColumns: new[] { "QuizId", "UserId" },
                keyValues: new object[] { 1, 2 },
                column: "BookmarkedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Challenges",
                keyColumn: "ChallengeId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 1,
                columns: new[] { "Content", "CreatedAt" },
                values: new object[] { "Học android rất là hay luôn", 1746013200000L });

            migrationBuilder.InsertData(
                table: "Comments",
                columns: new[] { "CommentId", "Content", "CreatedAt", "DocDetailId", "UserId" },
                values: new object[,]
                {
                    { 2, "đỡ tốn tiền học trung tâm", 1746013200000L, 1, 4 },
                    { 3, "Ảnh bị sao vậy sao không hiển thị", 1746013200000L, 1, 5 },
                    { 4, "có 20 trang thôi à, thêm đi", 1746013200000L, 2, 3 }
                });

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 11,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 12,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 13,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 14,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 15,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 16,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 17,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 18,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 19,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 20,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 2,
                columns: new[] { "CreatedAt", "IsPublic" },
                values: new object[] { 1746013200000L, true });

            migrationBuilder.InsertData(
                table: "Documents",
                columns: new[] { "DocId", "AuthorId", "Content", "CreatedAt", "IsPublic", "Title", "Views" },
                values: new object[,]
                {
                    { 3, 1, "Tài liệu cung cấp kiến thức cơ bản về cấu trúc và chức năng của tế bào.", 1746013200000L, true, "Sinh học tế bào", 32 },
                    { 4, 3, "Tổng hợp truyện cổ tích, ca dao, tục ngữ đặc sắc trong kho tàng dân gian.", 1746013200000L, true, "Văn học dân gian Việt Nam", 21 },
                    { 5, 4, "Trình bày khí hậu, địa hình, sông ngòi và vùng miền trên lãnh thổ Việt Nam.", 1746013200000L, true, "Địa lý tự nhiên Việt Nam", 14 },
                    { 6, 2, "Tìm hiểu về các hành tinh, vệ tinh và hiện tượng thiên văn cơ bản.", 1746013200000L, true, "Hệ mặt trời", 19 },
                    { 7, 1, "Phân tích các cuộc cách mạng lớn: Anh, Mỹ, Pháp và ảnh hưởng của chúng.", 1746013200000L, true, "Các cuộc cách mạng tư sản", 16 },
                    { 8, 3, "Khám phá cấu trúc, phân loại và vai trò của thực vật trong tự nhiên.", 1746013200000L, true, "Thực vật học", 20 },
                    { 9, 4, "Tài liệu nội bộ nghiên cứu tâm lý học sinh trong quá trình học tập.", 1746013200000L, false, "Tâm lý học giáo dục", 3 },
                    { 10, 2, "Tài liệu chuyên sâu về hành vi người tiêu dùng và doanh nghiệp trong thị trường.", 1746013200000L, false, "Kinh tế học vi mô", 2 },
                    { 11, 2, "Tài liệu chuyên sâu về hành vi người tiêu dùng và doanh nghiệp trong thị trường.", 1746013200000L, false, "", 2 }
                });

            migrationBuilder.UpdateData(
                table: "DownloadHistories",
                keyColumns: new[] { "TargetId", "TargetType", "UserId" },
                keyValues: new object[] { 1, "document", 3 },
                column: "DownloadedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 11,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 12,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 13,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 14,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 15,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 16,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 17,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 18,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 19,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 20,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 21,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 22,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 23,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 24,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 25,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 26,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 27,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 28,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 29,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 30,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 31,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 32,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 33,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 34,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 35,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 36,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 37,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 38,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 39,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 40,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Notifications",
                keyColumn: "NotificationId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.InsertData(
                table: "Quizzes",
                columns: new[] { "QuizId", "CreatedAt", "CreatorId", "Description", "IsPublic", "QuizName" },
                values: new object[,]
                {
                    { 3, 1746013200000L, 2, "Câu hỏi về quốc gia, thủ đô và danh lam thắng cảnh", true, "Địa lý thế giới" },
                    { 4, 1746013200000L, 4, "Kiến thức nền tảng về cơ học và nhiệt học", true, "Vật lý cơ bản" },
                    { 5, 1746013200000L, 3, "Các sự kiện và nhân vật lịch sử nổi bật", true, "Lịch sử Việt Nam" },
                    { 6, 1746013200000L, 5, "Câu hỏi về tế bào, di truyền và hệ sinh thái", true, "Sinh học nhập môn" },
                    { 7, 1746013200000L, 2, "Bài đọc kèm câu hỏi theo chuẩn IELTS", true, "Luyện đọc IELTS" },
                    { 8, 1746013200000L, 4, "Thuật ngữ và khái niệm cơ bản trong CNTT", true, "Thuật ngữ tin học" },
                    { 9, 1746013200000L, 3, "Nhận diện chữ cái Hiragana cơ bản", false, "Hiragana tiếng Nhật" },
                    { 10, 1746013200000L, 5, "Câu hỏi rèn luyện khả năng suy luận logic", false, "Tư duy logic" }
                });

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 1,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 2,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 3,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 4,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 5,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 6,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 7,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 8,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 9,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 10,
                column: "RatedAt",
                value: 1746013200000L);

            migrationBuilder.InsertData(
                table: "Tags",
                columns: new[] { "TagId", "Description", "TagName" },
                values: new object[,]
                {
                    { 6, "Khoa học tự nhiên", "Tự nhiên" },
                    { 7, "Khoa học xã hội", "Xã hội" },
                    { 8, "kinh tế học vĩ mô, kinh tế học vi mô", "Kinh tế" },
                    { 9, "Vật lý học tổng quát", "Vật lý" },
                    { 10, "Cơ học lượng tử, lý thuyết xuyên qua không thời gian", "Xuyên không học" }
                });

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 1,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 2,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 3,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 4,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 5,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.InsertData(
                table: "DocumentTagCrossRefs",
                columns: new[] { "DocId", "TagId" },
                values: new object[,]
                {
                    { 3, 6 },
                    { 3, 10 },
                    { 4, 7 },
                    { 5, 6 },
                    { 6, 6 },
                    { 7, 7 },
                    { 8, 6 },
                    { 9, 7 },
                    { 10, 8 }
                });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DeleteData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 2);

            migrationBuilder.DeleteData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 3);

            migrationBuilder.DeleteData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 4);

            migrationBuilder.DeleteData(
                table: "DocumentTagCrossRefs",
                keyColumns: new[] { "DocId", "TagId" },
                keyValues: new object[] { 3, 6 });

            migrationBuilder.DeleteData(
                table: "DocumentTagCrossRefs",
                keyColumns: new[] { "DocId", "TagId" },
                keyValues: new object[] { 3, 10 });

            migrationBuilder.DeleteData(
                table: "DocumentTagCrossRefs",
                keyColumns: new[] { "DocId", "TagId" },
                keyValues: new object[] { 4, 7 });

            migrationBuilder.DeleteData(
                table: "DocumentTagCrossRefs",
                keyColumns: new[] { "DocId", "TagId" },
                keyValues: new object[] { 5, 6 });

            migrationBuilder.DeleteData(
                table: "DocumentTagCrossRefs",
                keyColumns: new[] { "DocId", "TagId" },
                keyValues: new object[] { 6, 6 });

            migrationBuilder.DeleteData(
                table: "DocumentTagCrossRefs",
                keyColumns: new[] { "DocId", "TagId" },
                keyValues: new object[] { 7, 7 });

            migrationBuilder.DeleteData(
                table: "DocumentTagCrossRefs",
                keyColumns: new[] { "DocId", "TagId" },
                keyValues: new object[] { 8, 6 });

            migrationBuilder.DeleteData(
                table: "DocumentTagCrossRefs",
                keyColumns: new[] { "DocId", "TagId" },
                keyValues: new object[] { 9, 7 });

            migrationBuilder.DeleteData(
                table: "DocumentTagCrossRefs",
                keyColumns: new[] { "DocId", "TagId" },
                keyValues: new object[] { 10, 8 });

            migrationBuilder.DeleteData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 11);

            migrationBuilder.DeleteData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 3);

            migrationBuilder.DeleteData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 4);

            migrationBuilder.DeleteData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 5);

            migrationBuilder.DeleteData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 6);

            migrationBuilder.DeleteData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 7);

            migrationBuilder.DeleteData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 8);

            migrationBuilder.DeleteData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 9);

            migrationBuilder.DeleteData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 10);

            migrationBuilder.DeleteData(
                table: "Tags",
                keyColumn: "TagId",
                keyValue: 9);

            migrationBuilder.DeleteData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 3);

            migrationBuilder.DeleteData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 4);

            migrationBuilder.DeleteData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 5);

            migrationBuilder.DeleteData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 6);

            migrationBuilder.DeleteData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 7);

            migrationBuilder.DeleteData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 8);

            migrationBuilder.DeleteData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 9);

            migrationBuilder.DeleteData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 10);

            migrationBuilder.DeleteData(
                table: "Tags",
                keyColumn: "TagId",
                keyValue: 6);

            migrationBuilder.DeleteData(
                table: "Tags",
                keyColumn: "TagId",
                keyValue: 7);

            migrationBuilder.DeleteData(
                table: "Tags",
                keyColumn: "TagId",
                keyValue: 8);

            migrationBuilder.DeleteData(
                table: "Tags",
                keyColumn: "TagId",
                keyValue: 10);

            migrationBuilder.UpdateData(
                table: "Bookmarks",
                keyColumns: new[] { "QuizId", "UserId" },
                keyValues: new object[] { 1, 2 },
                column: "BookmarkedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Challenges",
                keyColumn: "ChallengeId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 1,
                columns: new[] { "Content", "CreatedAt" },
                values: new object[] { "Nice image!", 1743752400000L });

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 11,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 12,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 13,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 14,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 15,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 16,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 17,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 18,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 19,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 20,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 2,
                columns: new[] { "CreatedAt", "IsPublic" },
                values: new object[] { 1743752400000L, false });

            migrationBuilder.UpdateData(
                table: "DownloadHistories",
                keyColumns: new[] { "TargetId", "TargetType", "UserId" },
                keyValues: new object[] { 1, "document", 3 },
                column: "DownloadedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 11,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 12,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 13,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 14,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 15,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 16,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 17,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 18,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 19,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 20,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 21,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 22,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 23,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 24,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 25,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 26,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 27,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 28,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 29,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 30,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 31,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 32,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 33,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 34,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 35,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 36,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 37,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 38,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 39,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 40,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Notifications",
                keyColumn: "NotificationId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 1,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 2,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 3,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 4,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 5,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 6,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 7,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 8,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 9,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 10,
                column: "RatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 1,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 2,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 3,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 4,
                column: "CreatedAt",
                value: 1743752400000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 5,
                column: "CreatedAt",
                value: 1743752400000L);
        }
    }
}
