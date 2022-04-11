# presigned-s3-nbb-lambda
This project shows how to use [nbb](https://github.com/babashka/nbb) for a simple AWS Node.js lambda.
Also, it demonstrates lambda function URLs. More details could be found in the [post](https://dev.to/TODO-FIX-LINK) 

## lambda-api-gw
This module describes the serverless stack that creates presigned urls for file uploads. 
It consists of a Node.js lambda implemented using `nbb`, API Gateway that provides an endpoint and IAM role.
 * in order to install dependencies for this module execute the following:
```bash
cd lambda-api-gw/
npm install
```
* to provision the stack use the following command:
```
sls deploy
```
Make sure you've set correct values for the `region`, `buckets` and `accounts` parameters in [serverless-common.yml](lambda-api-gw/serverless-common.yml).
If provisioned successfully the output should be similar to:
```bash
endpoint: GET - https://nhhx4a5v4k.execute-api.eu-west-1.amazonaws.com/dev/upload-url/{env}/{folderName}/{fileName}
functions:
presigned-upload-url: s3-presigned-upload-api-gw-url-dev-presigned-upload-url (3.4 MB)
```

You can verify that the lambda is working with the following command:

```bash
https://nhhx4a5v4k.execute-api.eu-west-1.amazonaws.com/dev/upload-url/dev/folder1/file1.csv
```
The output should look like 
<details><summary>nbb-lambda successful response</summary>

```json
{
  "url": "https://s3.eu-west-1.amazonaws.com/dev-docs-upload-bucket",
  "fields": {
    "key": "folder1/file1.csv",
    "bucket": "dev-docs-upload-bucket",
    "X-Amz-Algorithm": "AWS4-HMAC-SHA256",
    "X-Amz-Credential": "ASXXXXXXXXXXXXXXXXXX/20220411/eu-west-1/s3/aws4_request",
    "X-Amz-Date": "20220411T141801Z",
    "X-Amz-Security-Token": "IQoJb3JpZ2luX2VjEIf//////////wEaCWV1L...",
    "Policy": "eyJleHBpcmF0aW9uIjoiMjAyMi0wNC0xMVQxNDoyM...",
    "X-Amz-Signature": "831a06ca70b..."
  }
}
```
</details>

* to remove the stack use the following command
```
sls remove
```


## lambda-url
This module describes the serverless stack that creates presigned urls for file uploads.
It consists of a Node.js lambda implemented using `nbb` and IAM role. This module leverages recently announced [AWS Function URLs](https://aws.amazon.com/blogs/aws/announcing-aws-lambda-function-urls-built-in-https-endpoints-for-single-function-microservices/).

⚠️ Serverless framework support this functionality only after version 3.12.
* in order to install dependencies for this module execute the following:
```bash
cd lambda-url/
npm install
```
* to provision the stack use the following command:
```
sls deploy
```
Make sure you've set correct values for the `region`, `buckets` and `accounts` parameters in [serverless-common.yml](lambda-url/serverless-common.yml).
If provisioned successfully the output should be similar to:
```bash
endpoint: https://kafts7qbofkzbbvxbxxzavlv6i0aelqr.lambda-url.eu-west-1.on.aws/
functions:
  presigned-upload-lambda-url: s3-presigned-upload-lambda-url-dev-presigned-upload-lambda-url (3.4 MB)
```
You can verify that the lambda is working with the following command:
```bash
curl https://kafts7qbofkzbbvxbxxzavlv6i0aelqr.lambda-url.eu-west-1.on.aws/dev/folder1/file1.csv
```
The output should look like
<details><summary>nbb-lambda successful response</summary>

```json
{
  "url": "https://s3.eu-west-1.amazonaws.com/dev-docs-upload-bucket",
  "fields": {
    "key": "folder1/file1.csv",
    "bucket": "dev-docs-upload-bucket",
    "X-Amz-Algorithm": "AWS4-HMAC-SHA256",
    "X-Amz-Credential": "ASXXXXXXXXXXXXXXXXXX/20220411/eu-west-1/s3/aws4_request",
    "X-Amz-Date": "20220411T141801Z",
    "X-Amz-Security-Token": "IQoJb3JpZ2luX2VjEIf//////////wEaCWV1L...",
    "Policy": "eyJleHBpcmF0aW9uIjoiMjAyMi0wNC0xMVQxNDoyM...",
    "X-Amz-Signature": "831a06ca70b..."
  }
}
```
</details>

* to remove the stack use the following command
```
sls remove
```