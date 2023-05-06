import socket
import sys
import requests
import requests_oauthlib
import os

import json
ACCESS_TOKEN = '1653465804401197071-DtBM8hrreIZlqaJzXzuoqem3RYesAH'
ACCESS_SECRET = 'MUDn2sDBGzQLxGnls9BPLC9k3eGZqK4gIi0F06zHxFhoW'
CONSUMER_KEY = 'brWFAyp99FidVhOwbPmDfqe9S'
CONSUMER_SECRET = 'Ty94bAI0xT4QG93hhUKNS7UBmCY0n1YPZrXrs1W4ayqKFlBXQ6'
my_auth = requests_oauthlib.OAuth1(CONSUMER_KEY, CONSUMER_SECRET,ACCESS_TOKEN, ACCESS_SECRET)


bearer_token = os.environ.get("BEARER_TOKEN")


def bearer_oauth(r):
    """
    Method required by bearer token authentication.
    """

    r.headers["Authorization"] = f"Bearer {bearer_token}"
    r.headers["User-Agent"] = "v2FilteredStreamPython"
    return r


def set_rules():
    # You can adjust the rules if needed
    sample_rules = [
        {"value": "covid "},
        {"value": "corona "},
        {"value": "epidemic "},
        {"value": "épidémie "},
        {"value": "pandemic "},
        {"value": "quarantine "},
        {"value": "virus "},
    ]
    payload = {"add": sample_rules}
    response = requests.post(
        "https://api.twitter.com/2/tweets/search/stream/rules",
        auth=bearer_oauth,
        json=payload,
    )
    if response.status_code != 201:
        raise Exception(
            "Cannot add rules (HTTP {}): {}".format(response.status_code, response.text)
        )
    print(json.dumps(response.json()))





def get_tweets():
    url = 'https://api.twitter.com/2/tweets/search/stream/rules'
    
    response = requests.get(query_url, auth=my_auth, stream=True)
    print(query_url, response)
    return response

def send_tweets_to_spark(http_resp, tcp_connection):
    for line in http_resp.iter_lines():
        try:
            full_tweet = json.loads(line)
            tweet_text = full_tweet['text']
            print("Tweet Text: " + tweet_text)
            print ("------------------------------------------")
            #tcp_connection.send(tweet_text + '\n')
        except:
            e = sys.exc_info()[0]
            print("Error: %s" % e)

"""
TCP_IP = "localhost"
TCP_PORT = 9009
conn = None
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_PORT))
s.listen(1)
print("Waiting for TCP connection...")
conn, addr = s.accept()
"""
print("Connected... Starting getting tweets.")
set_rules()