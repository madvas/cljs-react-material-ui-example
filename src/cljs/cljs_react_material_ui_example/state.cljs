(ns cljs-react-material-ui-example.state)

(defonce init-state
         {:person/list     [[:person/by-id 1]
                            [:person/by-id 2]]
          :person/by-id    {1 {:db/id            1
                               :person/name      "John"
                               :person/date      #inst "2016-04-08T22:00:00.000-00:00"
                               :person/status    [:status/by-id 1]
                               :person/happiness [:happiness/by-id 3]}
                            2 {:db/id            2
                               :person/name      "Susan"
                               :person/date      #inst "2016-04-08T22:00:00.000-00:00"
                               :person/status    [:status/by-id 4]
                               :person/happiness [:happiness/by-id 2]}}
          :person/new      {:person/name      ""
                            :person/date      nil
                            :person/status    nil
                            :person/happiness [:happiness/by-id 2]}
          :status/list     [[:status/by-id 1] [:status/by-id 2] [:status/by-id 3] [:status/by-id 4]
                            [:status/by-id 5]]
          :status/by-id    {1 {:db/id 1 :status/name "Employed"}
                            2 {:db/id 2 :status/name "Unemployed"}
                            3 {:db/id 3 :status/name "Freelancer"}
                            4 {:db/id 4 :status/name "Entrepreneur"}
                            5 {:db/id 5 :status/name "Parental leave"}}
          :happiness/list  [[:happiness/by-id 1] [:happiness/by-id 2] [:happiness/by-id 3]]
          :happiness/by-id {1 {:db/id 1 :happiness/name "Sad"}
                            2 {:db/id 2 :happiness/name "Normal"}
                            3 {:db/id 3 :happiness/name "Superb"}}})