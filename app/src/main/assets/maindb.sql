PRAGMA foreign_key = ON;

CREATE TABLE "coin_pairs" (
	"id"	INTEGER,
	"pair_coin"	TEXT NOT NULL UNIQUE COLLATE NOCASE,
	PRIMARY KEY("id" AUTOINCREMENT)
);

CREATE TABLE "coin_pairs_snapshot" (
	"id"	INTEGER,
	"time"	Integer NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);

CREATE TABLE "coin_pairs_snapshot_details"(
	"id"	INTEGER,
	"snapshot_id"	INTEGER	NOT NULL,
	"pair"	TEXT NOT NULL,
	"price"	INTEGER	NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("snapshot_id") REFERENCES "coin_pairs_snapshot"("id") ON DELETE CASCADE
)