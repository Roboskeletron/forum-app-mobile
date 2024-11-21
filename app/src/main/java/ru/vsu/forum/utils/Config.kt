package ru.vsu.forum.utils

object Config {
    const val SHARED_PREFERENCES_NAME = "AUTH_STATE_PREFERENCE"
    const val AUTH_STATE = "AUTH_STATE"
    const val URL_AUTHORIZATION = "https://accounts.google.com/o/oauth2/v2/auth"
    const val URL_TOKEN_EXCHANGE = "https://www.googleapis.com/oauth2/v4/token"
    const val URL_AUTH_REDIRECT = "com.ptruiz.authtest:/oauth2redirect"
    const val URL_LOGOUT = "https://accounts.google.com/o/oauth2/revoke?token="
    const val URL_LOGOUT_REDIRECT = "com.ptruiz.authtest:/logout"
    const val SCOPE_PROFILE = "profile"
    const val SCOPE_EMAIL = "email"
    const val SCOPE_OPENID = "openid"

    const val CLIENT_ID =
        "1080220280079-d0c0ba1076kdsr3d0pbuvd7e3npj1ket.apps.googleusercontent.com"
    const val CODE_VERIFIER_CHALLENGE_METHOD = "S256"
    const val MESSAGE_DIGEST_ALGORITHM = "SHA-256"

    const val BASE_URL = "http:10.0.2.2:5218/api/"
    const val AUTH_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJnYXh4VzVkbWF2WFN3Q3g4ZVVISXQ2ZjdGVFVWeVlHcXkxNlFGT3FTcXlzIn0.eyJleHAiOjE3MzIxOTM4OTQsImlhdCI6MTczMjE5MzU5NCwiYXV0aF90aW1lIjoxNzMyMTkzNTkzLCJqdGkiOiI1MDZlYmQzMi0xOGJkLTQ2NmQtYTM3NS1lOTZlODVkOGIxYTUiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjEwMDAvcmVhbG1zL2ZvcnVtLWFwcCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5YzdjZjQ5Mi1iMGRiLTQwMTYtODMyZS0yMzc3NDQ4MTBlMzMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ3ZWItZGV2Iiwic2lkIjoiMTBiNGJkMjYtYTQ1OS00N2JhLWJlNWItZWFjOWZlZWUwMjg4IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjUyMTgqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWZvcnVtLWFwcCIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJ1c2VyIHVzZXIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6InVzZXIiLCJmYW1pbHlfbmFtZSI6InVzZXIiLCJlbWFpbCI6InVzZXJAbWFpbC5jb20ifQ.liiQ0-R2DExqGI2hIQXVe-nVt8M1RNLwAAsV0DnIWIKZN_UOZuZF3rYhXPDN8xbcZkUYRmoDMfxp5K5hhIyDmTVcFMlAL3CfHPgTdddGG4vxHwC8Gm1GbnyZRZzmIfEmS_Tl3cx9HwglGh-124mT5bhbfTGvYfbM9lNmh50V_E7ZKfOb-ynAaidGec0k1TPizhaI7j2X6_DCf2C3Ao8PWygskaGEDUKVKS0vk8qFlsbLJ4uThz8rvDgdevvAxxB8Pq0T2HWv5G-nTUkOts39b8X9tWKX2nU0Nl1XqyNHWLhxonukfXPM4pcJ785Mot80gp7ivmvtsU6FhRHK3SLnsw" //TODO: remove when auth implemented
}