USE [master]
GO
/****** Object:  Database [RSS]    Script Date: 13.07.2022 22:52:34 ******/
CREATE DATABASE [RSS]
GO
ALTER DATABASE [RSS] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [RSS].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [RSS] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [RSS] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [RSS] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [RSS] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [RSS] SET ARITHABORT OFF 
GO
ALTER DATABASE [RSS] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [RSS] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [RSS] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [RSS] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [RSS] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [RSS] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [RSS] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [RSS] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [RSS] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [RSS] SET  DISABLE_BROKER 
GO
ALTER DATABASE [RSS] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [RSS] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [RSS] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [RSS] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [RSS] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [RSS] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [RSS] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [RSS] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [RSS] SET  MULTI_USER 
GO
ALTER DATABASE [RSS] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [RSS] SET DB_CHAINING OFF 
GO
ALTER DATABASE [RSS] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [RSS] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [RSS] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [RSS] SET QUERY_STORE = OFF
GO
USE [RSS]
GO
/****** Object:  Table [dbo].[Account]    Script Date: 13.07.2022 22:52:35 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Account](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[username] [nvarchar](50) NOT NULL,
	[password] [char](64) NOT NULL,
	[email] [varchar](50) NOT NULL,
	[frequency] [bigint] NOT NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Category]    Script Date: 13.07.2022 22:52:35 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Category](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[category] [varchar](50) NOT NULL,
	[userID] [bigint] NOT NULL,
 CONSTRAINT [PK_Category] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ChannelCategory]    Script Date: 13.07.2022 22:52:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChannelCategory](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[channelID] [bigint] NOT NULL,
	[categoryID] [bigint] NOT NULL,
 CONSTRAINT [PK_Channel_Category] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MailFrequency]    Script Date: 13.07.2022 22:52:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MailFrequency](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[frequency] [varchar](20) NOT NULL,
 CONSTRAINT [PK_MailFrequency] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NewsArchive]    Script Date: 13.07.2022 22:52:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NewsArchive](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[userChannelID] [bigint] NOT NULL,
	[title] [nvarchar](max) NOT NULL,
	[link] [nvarchar](2083) NOT NULL,
	[image] [nvarchar](2083) NULL,
	[pubDate] [smalldatetime] NOT NULL,
 CONSTRAINT [PK_ArchiveNews] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UserChannel]    Script Date: 13.07.2022 22:52:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UserChannel](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[userID] [bigint] NOT NULL,
	[channelURL] [nvarchar](2083) NOT NULL,
 CONSTRAINT [PK_UserChannel] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Account] ON 

INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (1, N'admin', N'34286ddb42a47c6b34f5d25d4c6c2863f1ffa672f38cf720b3c2d5b3e78fb831', N'admin@gmail.com', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (2, N'user', N'34286ddb42a47c6b34f5d25d4c6c2863f1ffa672f38cf720b3c2d5b3e78fb831', N'emmail', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (3, N'user1', N'34286ddb42a47c6b34f5d25d4c6c2863f1ffa672f38cf720b3c2d5b3e78fb831', N'emmail', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (4, N'user1', N'34286ddb42a47c6b34f5d25d4c6c2863f1ffa672f38cf720b3c2d5b3e78fb831', N'emmail', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (6, N'test1', N'f4539580d42e4d6e92e25a19ab4e27093d01f9626a74b98cdfc71f8dfb445fb4', N'test@test.pl', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (8, N'test4', N'f4539580d42e4d6e92e25a19ab4e27093d01f9626a74b98cdfc71f8dfb445fb4', N'test2@s.pl', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (9, N'test5', N'f4539580d42e4d6e92e25a19ab4e27093d01f9626a74b98cdfc71f8dfb445fb4', N'test@gmai.p', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (10, N'test6', N'f4539580d42e4d6e92e25a19ab4e27093d01f9626a74b98cdfc71f8dfb445fb4', N'test@as.pl', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (11, N'test123', N'34286ddb42a47c6b34f5d25d4c6c2863f1ffa672f38cf720b3c2d5b3e78fb831', N'saea@test.pl', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (12, N'test', N'34286ddb42a47c6b34f5d25d4c6c2863f1ffa672f38cf720b3c2d5b3e78fb831', N'dasda@gmail.com', 1)
INSERT [dbo].[Account] ([id], [username], [password], [email], [frequency]) VALUES (13, N'nowy1', N'2a41f69056c7c3b883c6c65ff3e11031d813039e072369968a5efd83a8ce4ee1', N'dsf@gmail.com', 1)
SET IDENTITY_INSERT [dbo].[Account] OFF
SET IDENTITY_INSERT [dbo].[Category] ON 

INSERT [dbo].[Category] ([id], [category], [userID]) VALUES (7, N'Wiadomości', 12)
SET IDENTITY_INSERT [dbo].[Category] OFF
SET IDENTITY_INSERT [dbo].[ChannelCategory] ON 

INSERT [dbo].[ChannelCategory] ([id], [channelID], [categoryID]) VALUES (11, 8, 7)
SET IDENTITY_INSERT [dbo].[ChannelCategory] OFF
SET IDENTITY_INSERT [dbo].[MailFrequency] ON 

INSERT [dbo].[MailFrequency] ([id], [frequency]) VALUES (1, N'W ogóle')
INSERT [dbo].[MailFrequency] ([id], [frequency]) VALUES (2, N'Co tydzień')
INSERT [dbo].[MailFrequency] ([id], [frequency]) VALUES (3, N'Co drugi dzień')
INSERT [dbo].[MailFrequency] ([id], [frequency]) VALUES (4, N'Codziennie')
SET IDENTITY_INSERT [dbo].[MailFrequency] OFF
SET IDENTITY_INSERT [dbo].[UserChannel] ON 

INSERT [dbo].[UserChannel] ([id], [userID], [channelURL]) VALUES (5, 11, N'http://rss.cnn.com/rss/edition.rss')
INSERT [dbo].[UserChannel] ([id], [userID], [channelURL]) VALUES (6, 12, N'http://rss.cnn.com/rss/edition.rss')
INSERT [dbo].[UserChannel] ([id], [userID], [channelURL]) VALUES (7, 12, N'https://fakty.interia.pl/feed')
INSERT [dbo].[UserChannel] ([id], [userID], [channelURL]) VALUES (8, 12, N'https://www.tvn24.pl/najnowsze.xml')
INSERT [dbo].[UserChannel] ([id], [userID], [channelURL]) VALUES (11, 12, N'https://www.eurogamer.pl/?format=rss')
INSERT [dbo].[UserChannel] ([id], [userID], [channelURL]) VALUES (10009, 13, N'http://rss.cnn.com/rss/edition.rss')
INSERT [dbo].[UserChannel] ([id], [userID], [channelURL]) VALUES (10010, 13, N'https://fakty.interia.pl/feed')
INSERT [dbo].[UserChannel] ([id], [userID], [channelURL]) VALUES (10011, 13, N'https://www.tvn24.pl/najnowsze.xml')
SET IDENTITY_INSERT [dbo].[UserChannel] OFF
ALTER TABLE [dbo].[Account]  WITH CHECK ADD  CONSTRAINT [FK_User_MailFrequency] FOREIGN KEY([frequency])
REFERENCES [dbo].[MailFrequency] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Account] CHECK CONSTRAINT [FK_User_MailFrequency]
GO
ALTER TABLE [dbo].[Category]  WITH CHECK ADD  CONSTRAINT [FK_Category_User] FOREIGN KEY([userID])
REFERENCES [dbo].[Account] ([id])
GO
ALTER TABLE [dbo].[Category] CHECK CONSTRAINT [FK_Category_User]
GO
ALTER TABLE [dbo].[ChannelCategory]  WITH CHECK ADD  CONSTRAINT [FK_Channel_Category_Category] FOREIGN KEY([categoryID])
REFERENCES [dbo].[Category] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ChannelCategory] CHECK CONSTRAINT [FK_Channel_Category_Category]
GO
ALTER TABLE [dbo].[ChannelCategory]  WITH CHECK ADD  CONSTRAINT [FK_Channel_Category_UserChannel] FOREIGN KEY([channelID])
REFERENCES [dbo].[UserChannel] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ChannelCategory] CHECK CONSTRAINT [FK_Channel_Category_UserChannel]
GO
ALTER TABLE [dbo].[NewsArchive]  WITH CHECK ADD  CONSTRAINT [FK_ArchiveNews_UserChannel] FOREIGN KEY([userChannelID])
REFERENCES [dbo].[UserChannel] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[NewsArchive] CHECK CONSTRAINT [FK_ArchiveNews_UserChannel]
GO
ALTER TABLE [dbo].[UserChannel]  WITH CHECK ADD  CONSTRAINT [FK_UserChannel_User] FOREIGN KEY([userID])
REFERENCES [dbo].[Account] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[UserChannel] CHECK CONSTRAINT [FK_UserChannel_User]
GO
USE [master]
GO
ALTER DATABASE [RSS] SET  READ_WRITE 
GO
