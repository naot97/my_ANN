DROP TABLE Acting;
DROP TABLE Favorite;
DROP TABLE Comment;
DROP TABLE Reviewer;
DROP TABLE Movie;
DROP TABLE Actor;


CREATE TABLE Reviewer(
    UserId int IDENTITIER(1,1) PRIMARY KEY  ,
    Username varchar(255) NOT NULL UNIQUE,
    Password varchar(255) NOT NULL,
    PhoneNumber decimal,
    Address ntext
)

CREATE Table Movie(
    MovieId int IDENTITY(1,1) PRIMARY KEY ,
    MovieName nvarchar(255) NOT NULL UNIQUE,
    Rate float NOT NULL,
    DateRelease date NOT NULL,
    ViewNumber int NOT NULL,
)
CREATE TABLE Actor(
    ActorId int IDENTITY(1,1) PRIMARY KEY ,
    ActorName nvarchar(255) NOT NULL,
    ActorImage varchar(255),
    ActorInfo ntext
)
CREATE TABLE Comment(
    CommentId int IDENTITY(1,1) PRIMARY KEY ,
    UserId int,
    MovieId int,
    Content ntext NOT NULL,
    FOREIGN KEY (UserId) REFERENCES Reviewer(UserId),
    FOREIGN KEY (MovieId) REFERENCES Movie(MovieId),
)

CREATE TABLE Favorite(
    UserId int,
    MovieId int,
    PRIMARY KEY(UserId,MovieId),
    FOREIGN KEY (UserId) REFERENCES Reviewer(UserId),
    FOREIGN KEY (MovieId) REFERENCES Movie(MovieId),
)

CREATE TABLE Acting(
    ActorId int,
    MovieId int,
    PRIMARY KEY(MovieId,ActorId),
    FOREIGN KEY (ActorId) REFERENCES Actor(ActorId),
    FOREIGN KEY (MovieId) REFERENCES Movie(MovieId),
)



