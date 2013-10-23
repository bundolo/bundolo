/*
DROP TABLE IF EXISTS user_profile CASCADE;
CREATE SEQUENCE user_id_seq start 1;
DROP TYPE IF EXISTS user_profile_status_type;
CREATE TYPE user_profile_status_type AS ENUM ('active', 'disabled', 'banned', 'pending');
DROP TYPE IF EXISTS user_profile_gender_type;
CREATE TYPE user_profile_gender_type AS ENUM ('male', 'female', 'other');
CREATE TABLE IF NOT EXISTS user_profile (
  user_id BIGINT NOT NULL DEFAULT nextval('user_id_seq') CONSTRAINT user_profile_pkey PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  salt VARCHAR(255) NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  gender VARCHAR(255),
  birth_date TIMESTAMP WITH TIME ZONE,
  email VARCHAR(255) NOT NULL UNIQUE,
  show_personal BOOLEAN,
  signup_date TIMESTAMP WITH TIME ZONE NOT NULL,
  last_login_date TIMESTAMP WITH TIME ZONE,
  last_ip VARCHAR(255), --todo data type and length
  user_profile_status VARCHAR(255) NOT NULL,
  avatar_url VARCHAR(511),
  session_id VARCHAR(255) UNIQUE,
  nonce VARCHAR(255) UNIQUE, --todo length
  new_email VARCHAR(255) UNIQUE,
  description_content_id BIGINT --CONSTRAINT user_profile_content_fkey REFERENCES content ON DELETE SET NULL
);
ALTER SEQUENCE user_id_seq OWNED BY user_profile.user_id;
--make email and new_email unique, currently validated in web service

DROP TABLE IF EXISTS content CASCADE;
CREATE SEQUENCE content_id_seq start 1;
DROP TYPE IF EXISTS content_kind_type;
CREATE TYPE content_kind_type AS ENUM ('page_description', 'page_comment', 'forum_group', 'forum_post', 'text', 'text_description', 'text_comment', 
'item_list_description', 'item_list_comment', 'connection_group', 'connection_description', 'connection_comment', 'news', 'news_comment', 'contest_description', 'contest_comment', 
'episode_group', 'episode', 'episode_comment', 'event', 'event_comment', 
'label', 'label_comment', 'user_description', 'user_comment');
DROP TYPE IF EXISTS content_status_type;
CREATE TYPE content_status_type AS ENUM ('active', 'disabled', 'pending');
CREATE TABLE IF NOT EXISTS content (
  content_id BIGINT NOT NULL DEFAULT nextval('content_id_seq') CONSTRAINT content_pkey PRIMARY KEY,
  author_username VARCHAR(255) CONSTRAINT content_user_fkey REFERENCES user_profile (username) ON DELETE CASCADE,
  parent_content_id BIGINT CONSTRAINT content_content_fkey REFERENCES content ON DELETE CASCADE,
  kind VARCHAR(255) NOT NULL,
  content_name VARCHAR(255),
  content_text TEXT,
  locale VARCHAR(20) NOT NULL,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  content_status VARCHAR(255) NOT NULL
);
ALTER SEQUENCE content_id_seq OWNED BY content.content_id;
CREATE INDEX content_kind_idx ON content (kind);
--TODO not null constraints on name and text depending on kind (currently only client-side validation)
--TODO unique constraints on name and text depending on kind (see commented out part below)

DROP TABLE IF EXISTS page CASCADE;
CREATE SEQUENCE page_id_seq start 1;
DROP TYPE IF EXISTS page_kind_type;
--TODO CREATE TYPE page_kind_type AS ENUM ('home', 'forum', 'contest', 'event', 'user', 'text', 'list', 'link', 'news', 'episode', 'custom');
DROP TYPE IF EXISTS page_status_type;
CREATE TYPE page_status_type AS ENUM ('active', 'disabled', 'pending');
CREATE TABLE IF NOT EXISTS page (
  page_id BIGINT NOT NULL DEFAULT nextval('page_id_seq') CONSTRAINT page_pkey PRIMARY KEY,
  author_username VARCHAR(255) CONSTRAINT page_user_fkey REFERENCES user_profile (username) ON DELETE CASCADE,
  parent_page_id BIGINT CONSTRAINT page_page_fkey REFERENCES page ON DELETE CASCADE,
  display_order INTEGER NOT NULL DEFAULT 0,
  page_status VARCHAR(255) NOT NULL,
  kind VARCHAR(255) NOT NULL,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  description_content_id BIGINT UNIQUE CONSTRAINT page_content_fkey REFERENCES content ON DELETE CASCADE
);
ALTER SEQUENCE page_id_seq OWNED BY page.page_id;
ALTER TABLE page ADD CONSTRAINT children_order_unique UNIQUE (parent_page_id, display_order);

DROP TABLE IF EXISTS item_list CASCADE;
CREATE SEQUENCE item_list_id_seq start 1;
DROP TYPE IF EXISTS item_list_kind_type;
CREATE TYPE item_list_kind_type AS ENUM ('Content', 'UserProfile', 'Page');
DROP TYPE IF EXISTS item_list_status_type;
CREATE TYPE item_list_status_type AS ENUM ('active', 'disabled', 'pending');
CREATE TABLE IF NOT EXISTS item_list (
  item_list_id BIGINT NOT NULL DEFAULT nextval('item_list_id_seq') CONSTRAINT item_list_pkey PRIMARY KEY,
  author_username VARCHAR(255) CONSTRAINT item_list_user_fkey REFERENCES user_profile (username) ON DELETE CASCADE,
  item_list_status VARCHAR(255) NOT NULL,
  kind VARCHAR(255) NOT NULL,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  query VARCHAR(511) NOT NULL,
  description_content_id BIGINT UNIQUE CONSTRAINT item_list_content_fkey REFERENCES content ON DELETE CASCADE
);
ALTER SEQUENCE item_list_id_seq OWNED BY item_list.item_list_id;

DROP TABLE IF EXISTS connection CASCADE;
CREATE SEQUENCE connection_id_seq start 1;
DROP TYPE IF EXISTS connection_kind_type;
CREATE TYPE connection_kind_type AS ENUM ('general'); --not used yet.
DROP TYPE IF EXISTS connection_status_type;
CREATE TYPE connection_status_type AS ENUM ('active', 'disabled', 'pending');
CREATE TABLE IF NOT EXISTS connection (
  connection_id BIGINT NOT NULL DEFAULT nextval('connection_id_seq') CONSTRAINT connection_pkey PRIMARY KEY,
  author_username VARCHAR(255) CONSTRAINT connection_user_fkey REFERENCES user_profile (username) ON DELETE CASCADE,
  parent_content_id BIGINT NOT NULL CONSTRAINT connection_pcontent_fkey REFERENCES content ON DELETE CASCADE,  
  description_content_id BIGINT UNIQUE CONSTRAINT connection_dcontent_fkey REFERENCES content ON DELETE CASCADE,
  kind VARCHAR(255) NOT NULL,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  connection_status VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  url VARCHAR(255)
);
ALTER SEQUENCE connection_id_seq OWNED BY connection.connection_id;
ALTER TABLE connection ADD CONSTRAINT email_or_url_not_null check (email is not null or url is not null);

DROP TABLE IF EXISTS contest CASCADE;
CREATE SEQUENCE contest_id_seq start 1;
DROP TYPE IF EXISTS contest_kind_type;
CREATE TYPE contest_kind_type AS ENUM ('general'); --not used yet.
DROP TYPE IF EXISTS contest_status_type;
CREATE TYPE contest_status_type AS ENUM ('active', 'disabled', 'pending');
CREATE TABLE IF NOT EXISTS contest (
  contest_id BIGINT NOT NULL DEFAULT nextval('contest_id_seq') CONSTRAINT contest_pkey PRIMARY KEY,
  author_username VARCHAR(255) CONSTRAINT contest_user_fkey REFERENCES user_profile (username) ON DELETE CASCADE,
  description_content_id BIGINT UNIQUE CONSTRAINT contest_content_fkey REFERENCES content ON DELETE CASCADE,
  kind VARCHAR(255) NOT NULL,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  expiration_date TIMESTAMP WITH TIME ZONE,
  contest_status VARCHAR(255) NOT NULL
);
ALTER SEQUENCE contest_id_seq OWNED BY contest.contest_id;

DROP TABLE IF EXISTS rating CASCADE;
CREATE SEQUENCE rating_id_seq start 1;
DROP TYPE IF EXISTS rating_kind_type;
CREATE TYPE rating_kind_type AS ENUM ('general'); --not used yet.
DROP TYPE IF EXISTS rating_status_type;
CREATE TYPE rating_status_type AS ENUM ('active', 'disabled', 'pending');
CREATE TABLE IF NOT EXISTS rating (
  rating_id BIGINT NOT NULL DEFAULT nextval('rating_id_seq') CONSTRAINT rating_pkey PRIMARY KEY,
  author_username VARCHAR(255) CONSTRAINT contest_user_fkey REFERENCES user_profile (username) ON DELETE CASCADE, --think about what should be done when user is deleted here.
  parent_content_id BIGINT CONSTRAINT rating_content_fkey REFERENCES content ON DELETE CASCADE,
  kind VARCHAR(255) NOT NULL,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  rating_status VARCHAR(255) NOT NULL,
  value BIGINT NOT NULL
);
ALTER SEQUENCE rating_id_seq OWNED BY rating.rating_id;

*/

/*

--these four could not be created because of content
CREATE UNIQUE INDEX text_names_unique on content (author_username, content_name, locale) WHERE ((kind = 'text') and (content_status = 'active' || content_status = 'pending'));
CREATE UNIQUE INDEX episode_group_names_unique on content (content_name, locale) WHERE kind = 'episode_group' and (content_status = 'active' OR content_status = 'pending');
CREATE UNIQUE INDEX episode_names_unique on content (content_name, parent_content_id, locale) WHERE kind = 'episode' and (content_status = 'active' OR content_status = 'pending');
CREATE UNIQUE INDEX contest_names_unique on content (content_name, locale) WHERE kind = 'contest_description' and (content_status = 'active' OR content_status = 'pending');

//this update is not done completely
UPDATE content c1 SET c1.content_name = c1.content_name || '1' FROM content c2 WHERE c1.content_name = c2.content_name AND c1.locale = c2.locale AND c1.content_id <> c2.content_id;
--this select gives rows that do not meet text title contraint but returns duplicates
SELECT row_number() OVER(), c1.* from content c1, content c2 WHERE c1.content_name = c2.content_name AND c1.locale = c2.locale AND c1.content_id <> c2.content_id AND (c1.content_status = 'active' OR c1.content_status = 'pending') AND c1.content_status = c2.content_status AND c1.kind = 'text' AND c2.kind = 'text' and c1.author_username = c2.author_username;

--just a reminder
DROP INDEX [ IF EXISTS ] name [, ...] [ CASCADE | RESTRICT ]

--these were added, but should be removed to be consistent with those that could not be added
CREATE UNIQUE INDEX forum_group_names_unique on content (content_name, locale) WHERE kind = 'forum_group' and (content_status = 'active' OR content_status = 'pending');
CREATE UNIQUE INDEX page_names_unique on content (content_name, locale) WHERE kind = 'page_description' and (content_status = 'active' OR content_status = 'pending');
CREATE UNIQUE INDEX item_list_names_unique on content (author_username, content_name, locale) WHERE kind = 'item_list_description' and (content_status = 'active' OR content_status = 'pending');
CREATE UNIQUE INDEX connection_group_names_unique on content (content_name, locale) WHERE kind = 'connection_group' and (content_status = 'active' OR content_status = 'pending');
CREATE UNIQUE INDEX connection_names_unique on content (content_name, locale) WHERE kind = 'connection_description' and (content_status = 'active' OR content_status = 'pending');
CREATE UNIQUE INDEX news_names_unique on content (content_name, locale) WHERE kind = 'news' and (content_status = 'active' OR content_status = 'pending');
CREATE UNIQUE INDEX event_names_unique on content (content_name, locale) WHERE kind = 'event' and (content_status = 'active' OR content_status = 'pending');
CREATE UNIQUE INDEX label_names_unique on content (content_name, locale) WHERE kind = 'label' and (content_status = 'active' OR content_status = 'pending');
 */


/*
INSERT INTO user_profile VALUES (0, 'a', 'a', 'salt','first_name', 'last_name', 'male', '2012-08-13', 'email', true, '2012-08-13 02:13:20+02', '2012-08-13 02:13:29+02', null, 'active', 'avatar_url', null, null, null, 900);
insert into user_profile select s.a, 'username' || s.a, 'password' || s.a, 'salt' || s.a,'first_name' || s.a, 'last_name' || s.a, 'male', current_date - s.a, 'email' || s.a, false, current_date - s.a, current_date - s.a, null, 'active', 'avatar_url' || s.a, null, null, null, 900+s.a from generate_series(1,50) AS s(a);

INSERT INTO user_profile VALUES (nextval('user_id_seq'), 'm', 'm', 'salt','first_name', 'last_name', 'male', '2012-08-13', 'm', true, '2012-08-13 02:13:20+02', '2012-08-13 02:13:29+02', null, 'active', 'avatar_url', null, null, null, null);
INSERT INTO user_profile VALUES (nextval('user_id_seq'), 'u', 'u', 'salt','first_name', 'last_name', 'male', '2012-08-13', 'u', true, '2012-08-13 02:13:20+02', '2012-08-13 02:13:29+02', null, 'active', 'avatar_url', null, null, null, null);
INSERT INTO content VALUES (nextval('content_id_seq'), 'm', null, 'user_description', null, 'moderator', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), 'u', null, 'user_description', null, 'user', 'en', '2012-09-29', 'active');
**/

1721	a	a	giE_U_d8bQ8	A12345	Kvaka	male	2013-01-05 00:00:00+01	a	t	2012-12-11 21:46:52.033+01	2013-01-27 06:05:06.501+01	127.0.0.1	active		1pc0i596rzkfx	Eygs9FGQs9F-P9gmcP41cxFO6z8		286998


INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Home', 'Home', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, null, 0, 'active', 'home', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Texts', 'Texts', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 0, 'active', 'texts', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Serials', 'Serials', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 1, 'active', 'serials', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Authors', 'Authors', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 2, 'active', 'authors', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Lists', 'Lists', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 3, 'disabled', 'lists', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Forum', 'Forum', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 4, 'active', 'forum', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'News', 'News', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 5, 'active', 'news', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Events', 'Events', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 6, 'disabled', 'events', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Contests', 'Contests', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 7, 'active', 'contests', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Connections', 'Connections', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 8, 'active', 'connections', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'Various', 'Various', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 9, 'active', 'custom', '2012-08-13 02:13:20+02', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'page_description', 'About', 'About', 'en', '2012-09-29', 'active');
INSERT INTO page VALUES (nextval('page_id_seq'), null, 1, 10, 'active', 'about', '2012-08-13 02:13:20+02', currval('content_id_seq'));

INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'English language', 'English', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Sidebar detach button label', 'Detach', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Sidebar attach button label', 'Attach', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add comment button label', 'Add Comment', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete comment button label', 'Delete Comment', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit comment button label', 'Edit Comment', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Save button label', 'Save', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Cancel button label', 'Cancel', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add page button label', 'Add Page', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete page button label', 'Delete Page', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit page button label', 'Edit Page', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Email address field label', 'Email Address', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Activation code field label', 'Activation Code', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Activate button label', 'Activate', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Display name field label', 'Display name', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Password field label', 'Password', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Email address validated status message', 'Email address verified. You can now use it to login.', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Email address validation failed status message', 'Email address verification failed!', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Account added status message', 'Account saved. You will get activation email.', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Account adding failed status message', 'Account adding failed!', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Account public data label', 'Public data', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Avatar field label', 'Avatar', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Account personal data label', 'Personal data', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'First name field label', 'First Name', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Last name field label', 'Last Name', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Gender field label', 'Gender', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Birth date field label', 'Birth Date', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Show personal field label', 'Show Personal Data Publicly', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Password instructions label', 'To keep existing password, leave these fields empty', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Current password field label', 'Current Password', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'New password field label', 'New Password', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Retype new password field label', 'Retype New Password', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Email instructions label', 'To keep existing email address, leave these fields empty', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'New email field label', 'New Email Address', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Retype new email field label', 'Retype New Email Address', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Save changes button label', 'Save Changes', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'You don''t have permission status message', 'You don''t have permission to access content you requested.', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Account updated status message', 'Account changes saved.', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Account update failed status message', 'Account update failed!', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Register button label', 'Register', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Login username or email field label', 'Username or email', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Remember me field label', 'Remember me', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Login button label', 'Login', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Forgot password button label', 'Forgot password?', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Profile button label', 'Profile', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Logout button label', 'Logout', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Send new password button label', 'Send new password', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Login message label', 'You are currently logged in as', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Logged out message label', 'You are currently anonymous guest', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Login failed message label', 'Login failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Logout failed message label', 'Logout failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete account button label', 'Delete', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit account button label', 'Edit', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Signup date field label', 'Signup date', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Comment added status message', 'Comment added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Comment adding failed status message', 'Comment adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Comment deleted status message', 'Comment deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Comment deleting failed status message', 'Comment deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Comment updated status message', 'Comment updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Comment update failed status message', 'Comment update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add list button label', 'Add List', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete list button label', 'Delete List', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit list button label', 'Edit List', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List name field label', 'Name', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List query field label', 'Query', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List kind field label', 'Kind', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List status field label', 'Status', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List added status message', 'List added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List adding failed status message', 'List adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List deleted status message', 'List deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List deleting failed status message', 'List deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List updated status message', 'List updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'List update failed status message', 'List update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Login password field label', 'Password', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Login email field label', 'Email', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add contest button label', 'Add contest', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete contest button label', 'Delete contest', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit contest button label', 'Edit contest', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest name field label', 'Title', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest description field label', 'Description', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest expiration date field label', 'Expiration date', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest added status message', 'Contest added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest adding failed status message', 'Contest adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest deleted status message', 'Contest deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest deleting failed status message', 'Contest deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest updated status message', 'Contest updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest update failed status message', 'Contest update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add news button label', 'Add news', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete news button label', 'Delete news', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit news button label', 'Edit news', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'News name field label', 'Title', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'News description field label', 'Description', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'News added status message', 'News added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'News adding failed status message', 'News adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'News deleted status message', 'News deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'News deleting failed status message', 'News deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'News updated status message', 'News updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'News update failed status message', 'News update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add forum post button label', 'Add forum post', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete forum post button label', 'Delete forum post', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit forum post button label', 'Edit forum post', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Forum post text field label', 'Text', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Forum post added status message', 'Forum post added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Forum post adding failed status message', 'Forum post adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Forum post deleted status message', 'Forum post deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Forum post deleting failed status message', 'Forum post deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Forum post updated status message', 'Forum post updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Forum post update failed status message', 'Forum post update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add link button label', 'Add link', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete link button label', 'Delete link', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit link button label', 'Edit link', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link name field label', 'Title', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link description field label', 'Description', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link email field label', 'Email address', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link url field label', 'Url', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link added status message', 'Link added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link adding failed status message', 'Link adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link deleted status message', 'Link deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link deleting failed status message', 'Link deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link updated status message', 'Link updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Link update failed status message', 'Link update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add serial button label', 'Add serial', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete serial button label', 'Delete serial', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit serial button label', 'Edit serial', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Serial name field label', 'Title', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Serial description field label', 'Description', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Serial added status message', 'Serial added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Serial adding failed status message', 'Serial adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Serial deleted status message', 'Serial deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Serial deleting failed status message', 'Serial deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Serial updated status message', 'Serial updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Serial update failed status message', 'Serial update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add episode button label', 'Add episode', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete episode button label', 'Delete episode', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit episode button label', 'Edit episode', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Episode name field label', 'Title', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Episode text field label', 'Text', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Episode added status message', 'Episode added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Episode adding failed status message', 'Episode adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Episode deleted status message', 'Episode deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Episode deleting failed status message', 'Episode deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Episode updated status message', 'Episode updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Episode update failed status message', 'Episode update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add text button label', 'Add text', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete text button label', 'Delete text', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Edit text button label', 'Edit text', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Text name field label', 'Title', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Text description field label', 'Short description', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Text text field label', 'Text', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Text added status message', 'Text added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Text adding failed status message', 'Text adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Text deleted status message', 'Text deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Text deleting failed status message', 'Text deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Text updated status message', 'Text updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Text update failed status message', 'Text update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Already logged in status message', 'Already logged in!', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Generic database error status message', 'Database error', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Error fetching comments status message', 'Error fetching comments', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page added status message', 'Page added', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page adding failed status message', 'Page adding failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page deleted status message', 'Page deleted', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page deleting failed status message', 'Page deleting failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page updated status message', 'Page updated', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page update failed status message', 'Page update failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Add message button label', 'Write private message', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Send message button label', 'Send', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Message title field label', 'Title', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Message text field label', 'Text', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Message sent status message', 'Message sent', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Message sending failed status message', 'Message sending failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not null validation message', 'Must be given', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Is null validation message', 'Should not be given', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Equal validation message', 'Should be equal', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not equal validation message', 'Should not be equal', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not empty validation message', 'Mustn''t be empty', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Min validation message', 'Minimum value for this field is ''{0}''. You entered ''{1}''', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Max validation message', 'Maximum value for this field is ''{0}''. You entered ''{1}''', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not in range validation message', 'Value ''{2}'' lies beyond the boundaries [{0},{1}]', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not an integer validation message', 'Number is not an integer', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not a long validation message', 'Not a whole number', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not a float validation message', 'Not a floating point number', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not a double validation message', 'Not a floating point number', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Length validation message', 'Needs a length between ''{0}'' and ''{1}''. Was ''{2}''', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Strings not equal validation message', 'Strings must be equal', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Must select value validation message', 'Please select a value', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator assert false validation message', 'Assertion failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator assert true validation message', 'Assertion failed', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator future validation message', 'Must be a future date', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator length validation message', 'Length not correct', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator max validation message', 'Too big', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator min validation message', 'Too small', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator not null validation message', 'Must be given', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator past validation message', 'Must be a past date', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator pattern validation message', 'Incorrect pattern', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator range validation message', 'Not in range', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator size validation message', 'Incorrect number of elements', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator email validation message', 'Not a well-formed email address', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator not empty validation message', 'May not be null or empty', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator digits validation message', 'Numeric value out of bounds', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator credit card validation message', 'Invalid credit card number', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator ean validation message', 'Invalid EAN', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'No date given validation message', 'No date entered', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Unparsable date validation message', 'Wrong date format', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not a regex validation message', 'No valid regular expression given', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Regex not fulfilled validation message', 'The text does not fulfill the given regular expression', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not a valid character validation message', 'Not a valid character: ''{0}''', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not a valid email validation message', '''{0}'' is not a valid email address', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not a valid time withouth seconds required validation message', '''{0}'' is not a valid time (seconds must not be given)', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not a valid time with seconds optionally validation message', '''{0}'' is not a valid time (seconds may be given)', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Not a valid time with seconds required validation message', '''{0}'' is not a valid time (seconds must be given)', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'User description field label', 'Short description', 'en', '2012-09-29', 'active');
--
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Comment text field label', 'Text', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page kind field label', 'Kind', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page name field label', 'Name', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page text field label', 'Text', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Page children field label', 'Page children', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Email already exists validation message', 'Email address already used', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Display name already exists validation message', 'Display name already used', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Validator either not empty validation message', 'At least one field must not be empty', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Passwords must match validation message', 'Passwords must match', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Emails must match validation message', 'Emails must match', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Wrong password validation message', 'Wrong password', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Error fetching description status message', 'Error fetching description', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Title table column label', 'Title', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Date table column label', 'Date', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Author table column label', 'Author', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Contest no expiration date label', 'No expiration date', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete text confirmation message', 'Are you sure you want to delete text?', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete serial confirmation message', 'Are you sure you want to delete serial?', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Delete episode confirmation message', 'Are you sure you want to delete episode?', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Author texts label', 'Author''s texts', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Anonymous user label', 'Guest', 'en', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'label', 'Username not allowed validation message', 'Username not allowed', 'en', '2012-09-29', 'active');
--


INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Latest texts', 'Latest texts', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE locale=''%0%'' AND kind=''text'' AND content_status=''active'' ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Forum groups', 'Forum groups', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE locale=''%0%'' AND kind=''forum_group'' AND content_status=''active'' ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Forum group topics', 'Forum group topics', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE locale=''%0%'' AND kind=''forum_topic'' AND (content_status=''active'' OR content_status=''disabled'') AND parent_content_id=%1% ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Serials', 'Serials', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE locale=''%0%'' AND kind=''episode_group'' AND (content_status=''active'' OR content_status=''pending'') ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Serial episodes', 'Serial episodes', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE locale=''%0%'' AND kind=''episode'' AND content_status=''active'' AND parent_content_id=%1% ORDER BY creation_date ASC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Authors', 'Authors', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'UserProfile', '2012-11-05', 'SELECT u FROM UserProfile u WHERE user_profile_status=''active'' AND username <> 'gost' ORDER BY signup_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Latest news', 'Latest news', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE locale=''%0%'' AND kind=''news'' AND content_status=''active'' ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Latest texts'), 'item_list_description', 'Novi tekstovi', 'Novi tekstovi', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Forum groups'), 'item_list_description', 'Kategorije na forumu', 'Kategorije na forumu', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Forum group topics'), 'item_list_description', 'Teme u kategoriji', 'Teme u kategoriji', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Serials'), 'item_list_description', 'Serije', 'Serije', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Serial episodes'), 'item_list_description', 'Nastavci serije', 'Nastavci serije', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Authors'), 'item_list_description', 'Autori', 'Autori', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Latest news'), 'item_list_description', 'Najnovije vesti', 'Najnovije vesti', 'sr', '2012-09-29', 'active');

INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Forum topic posts', 'Forum topic posts', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE locale=''%0%'' AND kind=''forum_post'' AND content_status=''active'' AND parent_content_id=%1% ORDER BY creation_date ASC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Forum topic posts'), 'item_list_description', 'Odgovori na temi', 'Odgovori na temi', 'sr', '2012-09-29', 'active');

INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Latest contests', 'Latest contests', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Contest', '2012-11-05', 'SELECT c FROM Contest c WHERE (current_date() < expiration_date OR expiration_date IS NULL) AND contest_status=''active'' ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Latest contests'), 'item_list_description', 'Najnoviji konkursi', 'Najnoviji konkursi', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Latest links', 'Latest links', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Connection', '2012-11-05', 'SELECT c FROM Connection c WHERE connection_status=''active'' ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Latest links'), 'item_list_description', 'Najnoviji linkovi', 'Najnoviji linkovi', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Latest forum posts', 'Latest forum posts', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE locale=''%0%'' AND kind=''forum_post'' AND content_status=''active'' ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Latest forum posts'), 'item_list_description', 'Najnoviji odgovori na forumu', 'Najnoviji odgovori na forumu', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Link groups', 'Link groups', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE locale=''%0%'' AND kind=''connection_group'' AND content_status=''active'' ORDER BY creation_date ASC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Link groups'), 'item_list_description', 'Kategorije linkova', 'Kategorije linkova', 'sr', '2012-09-29', 'active');
INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Link group entries', 'Link group entries', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Connection', '2012-11-05', 'SELECT c FROM Connection c WHERE parent_content_id=%0% AND connection_status=''active'' ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Link group entries'), 'item_list_description', 'Linkovi u kategoriji', 'Linkovi u kategoriji', 'sr', '2012-09-29', 'active');

INSERT INTO content VALUES (nextval('content_id_seq'), null, null, 'item_list_description', 'Author texts', 'Author texts', 'en', '2012-09-29', 'active');
INSERT INTO item_list VALUES (nextval('item_list_id_seq'), NULL, 'active', 'Content', '2012-11-05', 'SELECT c FROM Content c WHERE author_username=''%0%'' AND locale=''%1%'' AND kind=''text'' AND content_status=''active'' ORDER BY creation_date DESC', currval('content_id_seq'));
INSERT INTO content VALUES (nextval('content_id_seq'), null, (select content_id from content where kind='item_list_description' and locale='en' and content_name='Author texts'), 'item_list_description', 'Autorovi tekstovi', 'Autorovi tekstovi', 'sr', '2012-09-29', 'active');

/*

insert into content select s.a, 'username' || s.a - 900, null, 'user_description', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(901,950) AS s(a);
--insert into content select s.a, 'a', null, 'page_description', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(951,962) AS s(a);
insert into content select s.a, 'a', s.a - 163, 'page_description', s.a, s.a, 'sr', current_date - s.a, 'active' from generate_series(963,969) AS s(a);
insert into content select s.a, 'a', null, 'item_list_description', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(970,976) AS s(a);
INSERT INTO content VALUES (1000, 'a', 800, 'page_comment', 'content1000', 'vidra', 'en', '2012-08-13 02:13:20+02', 'active');
INSERT INTO content VALUES (1001, 'a', 800, 'page_comment', 'content1001', 'lasica', 'en', '2012-08-13 02:13:20+02', 'active');
INSERT INTO content VALUES (1002, 'a', 1000, 'page_comment', 'content1002', 'vidrac', 'en', '2012-08-13 02:13:20+02', 'active');
INSERT INTO content VALUES (1003, 'a', 1000, 'page_comment', 'content1003', 'vidrad', 'en', '2012-08-13 02:13:20+02', 'active');
INSERT INTO content VALUES (1004, 'a', null, 'text', 'content1004', 'text1', 'en', '2012-08-13 02:13:20+02', 'active');
INSERT INTO content VALUES (1005, 'a', null, 'text', 'content1005', 'text2', 'en', '2012-08-13 02:13:20+02', 'active');
INSERT INTO content VALUES (1006, 'a', null, 'forum_group', 'Literature', 'Literature', 'en', '2012-08-12', 'active');
INSERT INTO content VALUES (1007, 'a', null, 'forum_group', 'Bundolo', 'Bundolo', 'en', '2012-08-13', 'active');
INSERT INTO content VALUES (1008, 'a', null, 'forum_group', 'Various', 'Various', 'en', '2012-08-14', 'active');
INSERT INTO content VALUES (1009, 'a', null, 'forum_group', 'Suggestions and comments', 'Suggestions and comments', 'en', '2012-08-15', 'active');
INSERT INTO content VALUES (1010, 'a', null, 'forum_group', 'Archives', 'Archives', 'en', '2012-08-16', 'active');
insert into content select s.a, 'a', null, 'text', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1011,1100) AS s(a);
insert into content select s.a, 'a', 1006, 'forum_post', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1101,1200) AS s(a);
insert into content select s.a, 'a', 1007, 'forum_post', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1201,1300) AS s(a);
insert into content select s.a, 'a', 1008, 'forum_post', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1301,1400) AS s(a);
insert into content select s.a, 'a', 1009, 'forum_post', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1401,1500) AS s(a);
insert into content select s.a, 'a', 1010, 'forum_post', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1501,1600) AS s(a);
insert into content select s.a, 'a', 1101, 'forum_post', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1601,1650) AS s(a);
insert into content select s.a, 'a', 1102, 'forum_post', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1651,1700) AS s(a);
insert into content select s.a, 'a', null, 'episode_group', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1701,1720) AS s(a);
insert into content select s.a, 'a', 1701, 'episode', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1721,1740) AS s(a);
insert into content select s.a, 'a', 1702, 'episode', s.a, s.a, 'en', current_date - s.a, 'active' from generate_series(1741,1760) AS s(a);
insert into content select s.a, 'username' || s.a - 1760, null, 'text' || s.a, 'title' || s.a, 'text' || s.a, 'en', current_date - s.a, 'active' from generate_series(1761,1800) AS s(a);

*/

