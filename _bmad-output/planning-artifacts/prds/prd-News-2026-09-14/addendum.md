---
title: "PRD Addendum: NewsRoom"
created: 2026-09-14
updated: 2026-09-14
---

# PRD Addendum: NewsRoom

Chi tiết kỹ thuật, options-considered, và context bổ sung cho downstream documents (Architecture, UX Design). Không thuộc về PRD chính.

## Existing Codebase Inventory

Xem [Brief Addendum — Codebase Inventory](../briefs/brief-News-2026-09-14/addendum.md#existing-codebase-inventory-v130) cho danh sách đầy đủ backend/frontend components, technology stack, và infrastructure.

## User Role Permissions Matrix

| Permission | Guest | User | Journalist (V2) | Admin |
|------------|-------|------|-----------------|-------|
| View Public Posts | ✅ | ✅ | ✅ | ✅ |
| View Followers-only Posts | ❌ | ✅ (if following) | ✅ (if following) | ✅ |
| Create Post | ❌ | ✅ | ✅ | ✅ |
| Edit own Post | ❌ | ✅ | ✅ | ✅ |
| Delete own Post | ❌ | ✅ | ✅ | ✅ |
| Edit/Delete any Post | ❌ | ❌ | ❌ | ✅ |
| Like/Comment/Share | ❌ | ✅ | ✅ | ✅ |
| Follow/Unfollow | ❌ | ✅ | ✅ | ✅ |
| Report Post | ❌ | ✅ | ✅ | ✅ |
| Access Admin Dashboard | ❌ | ❌ | ❌ | ✅ |
| Manage Ads | ❌ | ❌ | ❌ | ✅ |
| Moderate Content | ❌ | ❌ | ❌ | ✅ |
| Verify Journalists | ❌ | ❌ | ❌ | ✅ |
| Journalist Badge | ❌ | ❌ | ✅ | ❌ |

## Data Model Considerations

### New Entities Needed (beyond existing v1.3.0)

| Entity | Purpose | Key fields | Relations |
|--------|---------|------------|-----------|
| UserProfile | Extended user profile | displayName, avatar, bio, username | 1:1 with User |
| Follow | Follow relationship | followerId, followingId, createdAt | User → User |
| Like | Post like | userId, postId, createdAt | User → Post |
| Comment | Post comment | userId, postId, content, createdAt | User → Post |
| Share | Post share | userId, postId, createdAt | User → Post |
| Report | Content report | reporterId, postId, reason, status | User → Post |
| Notification | In-app notification | userId, type, sourceId, read, createdAt | User → various |

### Existing Entities to Modify

| Entity | Modification |
|--------|-------------|
| User | Add: username, bio, avatar, emailVerified, role expansion (USER/JOURNALIST/ADMIN), accountStatus (active/warned/banned) |
| News (→ Post) | Add: visibility (PUBLIC/FOLLOWERS_ONLY/PRIVATE), status expansion (published/draft/pending_review/flagged/removed), authorId link to User |

## API Design Considerations

### New API Groups Needed

| Group | Endpoints (estimated) | Auth |
|-------|----------------------|------|
| /api/auth | register, login, logout, refresh-token, verify-email | Public (register, login, verify) / JWT (others) |
| /api/users | profile CRUD, search users | JWT |
| /api/feed | personal feed, public feed | Public (public) / JWT (personal) |
| /api/posts | create, edit, delete, get, list by user | JWT (write) / Public (read public) |
| /api/interactions | like/unlike, comment CRUD, share | JWT |
| /api/follow | follow, unfollow, followers list, following list | JWT |
| /api/reports | create report, admin list/action | JWT + Admin |
| /api/moderation | flagged posts, actions (approve/remove/warn/ban) | JWT + Admin |
| /api/notifications | list, mark read, count unread | JWT |

### Migration Strategy

Existing `/api/news` endpoints should be maintained for backward compatibility during transition, then gradually deprecated in favor of `/api/posts`. The News entity becomes Post with added social fields.

## Content Moderation — Keyword Blacklist Approach

### V1 Implementation Options Considered

| Option | Pros | Cons | Decision |
|--------|------|------|----------|
| Simple keyword match | Fast, simple | High false positive rate | ✅ Chosen for V1 |
| Regex patterns | More flexible | Complex to maintain | Partial — for common patterns |
| ML/AI classification | Most accurate | Requires training data, compute | ❌ Deferred to V2+ |
| Third-party API (Perspective API) | Good accuracy, no training | External dependency, cost | ❌ Deferred to V2+ |

### Keyword Categories

- Profanity / hate speech
- Spam patterns (repeated links, sales language)
- Sensitive political content (admin-configurable threshold)
- Known fake news patterns

## Rejected Alternatives — PRD Scope Decisions

| Decision | Alternative considered | Reason rejected |
|----------|----------------------|-----------------|
| Chronological feed (V1) | Algorithmic recommendation feed | Complexity — solo dev, chronological + trending good enough for V1 volume |
| Flat comments (V1) | Threaded/nested comments | UX complexity, DB query complexity, V2 when community demands it |
| In-app notifications only (V1) | Push notifications (FCM/APNs) | Requires service worker setup, mobile considerations — V2 |
| Email/password auth (V1) | Social login (Google/FB) | OAuth integration complexity, each provider requires registration — V2 |
| Local file storage (V1) | Cloud storage (S3/GCS) | Cost, configuration complexity — V2 when storage pressure grows |
| Internal share only (V1) | Share to external (FB/Zalo/X) | External API integrations — V2 |

## Competitive Feature Mapping

| Feature | NewsRoom V1 | Facebook | VnExpress | Reddit | X/Twitter |
|---------|------------|----------|-----------|--------|-----------|
| UGC Posts | ✅ | ✅ | ❌ | ✅ | ✅ |
| Journalist verification | ❌ (V2) | ❌ | N/A (all journalists) | ❌ | ✅ (paid) |
| Personal Feed | ✅ | ✅ | ❌ | ✅ | ✅ |
| Follow system | ✅ | ✅ (Friends) | ❌ | ✅ (Join) | ✅ |
| Like/Comment/Share | ✅ | ✅ | Comment only | ✅ (Vote) | ✅ |
| Content visibility | ✅ (3 levels) | ✅ | ❌ | ✅ (2 levels) | ✅ (2 levels) |
| Ads system | ✅ | ✅ | ✅ | ✅ | ✅ |
| News-focused UX | ✅ | ❌ | ✅ | Partial | ❌ |
| Community moderation | ✅ (report) | ✅ | ❌ | ✅ (vote) | ✅ (report) |
