(ns handler
  (:require ["@aws-sdk/client-s3" :refer [S3Client]]
            ["@aws-sdk/s3-presigned-post" :refer [createPresignedPost]]
            [clojure.string :as s]
            [goog.string.format]
            [applied-science.js-interop :as j]
            [promesa.core :as p]))

(def s3 (S3Client. #js{:region "eu-west-1"}))

(def bucket-name-template "%s-docs-upload-bucket")

(defn handler [event _ctx]
      (p/let [[env folderName fileName] (-> event
                                            (j/get-in [:requestContext :http :path])
                                            (s/split #"/")
                                            (rest))
              key (str (s/replace folderName #"_" "/") "/" fileName)
              bucket-name (goog.string/format bucket-name-template env)
              response (createPresignedPost s3 (clj->js {:Bucket     bucket-name
                                                         :Key        key
                                                         :Expires    300
                                                         :Fields     {:key key
                                                                      :acl "bucket-owner-full-control"}
                                                         :Conditions [["eq", "$key", key]
                                                                      ["content-length-range", 0, 10485760]
                                                                      ["starts-with", "$Content-Type", "text/"]]}))]
             (clj->js {:statusCode 200
                       :headers    {"Access-Control-Allow-Origin"  "*"
                                    "Access-Control-Allow-Headers" "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token,X-Amz-User-Agent,Access-Control-Allow-Origin",
                                    "Access-Control-Allow-Methods" "OPTIONS,GET"}
                       :body       (js/JSON.stringify response nil 2)})))

#js {:handler handler}