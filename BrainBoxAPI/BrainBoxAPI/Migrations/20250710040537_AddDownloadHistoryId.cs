using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace BrainBoxAPI.Migrations
{
    /// <inheritdoc />
    public partial class AddDownloadHistoryId : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_DownloadHistories",
                table: "DownloadHistories");

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumns: new[] { "TargetId", "TargetType", "UserId" },
                keyValues: new object[] { 1, "document", 3 });

            migrationBuilder.AlterColumn<string>(
                name: "TargetType",
                table: "DownloadHistories",
                type: "nvarchar(max)",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "nvarchar(450)");

            migrationBuilder.AddColumn<int>(
                name: "Id",
                table: "DownloadHistories",
                type: "int",
                nullable: false,
                defaultValue: 0)
                .Annotation("SqlServer:Identity", "1, 1");

            migrationBuilder.AddPrimaryKey(
                name: "PK_DownloadHistories",
                table: "DownloadHistories",
                column: "Id");

            migrationBuilder.UpdateData(
                table: "Bookmarks",
                keyColumns: new[] { "QuizId", "UserId" },
                keyValues: new object[] { 1, 2 },
                column: "BookmarkedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Challenges",
                keyColumn: "ChallengeId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 11,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 12,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 13,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 14,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 15,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 16,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 17,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 18,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 19,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "DocumentDetails",
                keyColumn: "DocDetailId",
                keyValue: 20,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 11,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.InsertData(
                table: "DownloadHistories",
                columns: new[] { "Id", "DownloadedAt", "TargetId", "TargetType", "UserId" },
                values: new object[,]
                {
                    { 1, 1751414888397L, 1, "document", 4 },
                    { 2, 1751651196732L, 2, "document", 3 },
                    { 3, 1750696820452L, 6, "quiz", 4 },
                    { 4, 1750670829108L, 5, "quiz", 4 },
                    { 5, 1749106261854L, 10, "document", 5 },
                    { 6, 1749480779530L, 6, "quiz", 1 },
                    { 7, 1751720678852L, 1, "quiz", 3 },
                    { 8, 1751903568658L, 5, "quiz", 1 },
                    { 9, 1750874085287L, 2, "quiz", 4 },
                    { 10, 1749305100835L, 1, "document", 2 }
                });

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 11,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 12,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 13,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 14,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 15,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 16,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 17,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 18,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 19,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 20,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 21,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 22,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 23,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 24,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 25,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 26,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 27,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 28,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 29,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 30,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 31,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 32,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 33,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 34,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 35,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 36,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 37,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 38,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 39,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Flashcards",
                keyColumn: "CardId",
                keyValue: 40,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Notifications",
                keyColumn: "NotificationId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 1,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 1,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 2,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 3,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 4,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 5,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 6,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 7,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 8,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 9,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "RatingQuizzes",
                keyColumn: "RatingId",
                keyValue: 10,
                column: "RatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 1,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 2,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 3,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 4,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.UpdateData(
                table: "Users",
                keyColumn: "Id",
                keyValue: 5,
                column: "CreatedAt",
                value: 1748720800000L);

            migrationBuilder.CreateIndex(
                name: "IX_DownloadHistories_UserId",
                table: "DownloadHistories",
                column: "UserId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_DownloadHistories",
                table: "DownloadHistories");

            migrationBuilder.DropIndex(
                name: "IX_DownloadHistories_UserId",
                table: "DownloadHistories");

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 1);

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 2);

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 3);

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 4);

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 5);

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 6);

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 7);

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 8);

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 9);

            migrationBuilder.DeleteData(
                table: "DownloadHistories",
                keyColumn: "Id",
                keyColumnType: "int",
                keyValue: 10);

            migrationBuilder.DropColumn(
                name: "Id",
                table: "DownloadHistories");

            migrationBuilder.AlterColumn<string>(
                name: "TargetType",
                table: "DownloadHistories",
                type: "nvarchar(450)",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "nvarchar(max)");

            migrationBuilder.AddPrimaryKey(
                name: "PK_DownloadHistories",
                table: "DownloadHistories",
                columns: new[] { "UserId", "TargetId", "TargetType" });

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
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 2,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Comments",
                keyColumn: "CommentId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1746013200000L);

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
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Documents",
                keyColumn: "DocId",
                keyValue: 11,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.InsertData(
                table: "DownloadHistories",
                columns: new[] { "TargetId", "TargetType", "UserId", "DownloadedAt" },
                values: new object[] { 1, "document", 3, 1746013200000L });

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

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 3,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 4,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 5,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 6,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 7,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 8,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 9,
                column: "CreatedAt",
                value: 1746013200000L);

            migrationBuilder.UpdateData(
                table: "Quizzes",
                keyColumn: "QuizId",
                keyValue: 10,
                column: "CreatedAt",
                value: 1746013200000L);

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
        }
    }
}
