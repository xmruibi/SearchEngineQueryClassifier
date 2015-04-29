
##1.	Background:
Collaborative Exploratory Web Search means user can combine their efforts in the information retrieval activities, share information resources collaboratively using knowledge tags, and allow experts to guide less experienced people through their searches. Collaboration partners do so by providing query terms, collective tagging, adding comments or opinions, rating search results, and links clicked of former (successful) IR activities to users having the same or related information need [1].

In this study, I concentrated on studying the existing system, which called “CollabSearch” from Information Retrieval Laboratory in University of Pittsburgh.  All of my study was based on this system and tried to improve its ranking result. 

For this system, it has both search and collaboration features. The main interface of this system contains three frames: topic statement, search and team workspace. The system can track user actions and analyze these actions for providing better search results ranking which seems like the most of individual searching engine. 

For the collaborative purpose, this system designed can examine search results in the returned list for relevant information, and can save a whole web page or a snippet of the page. All of the saved web pages and snippets, collected by the users in the same team, are stored in the team workspace frame. A notice is displayed at the top when new items have been saved to the team workspace. Users can click to view more details about an item in the workspace [2]. 

It is also mentionable that the system applied the chatting function, which is a platform for teammates’ communication during the web-searching task. Much of my study was trying to understand and find the potential meaning from the chat record. 
##2.	Purpose:
Based on the existing system, the purpose of my study can be separated into two parts. The first one is trying to understand every chat information and find out their potential annotation. Then using these manual annotations, we can identify the subtopics under the given task belong to a certain user in a group.  The second purpose is to match the user query into with its most possible subtopic by giving the probabilities between query and each subtopics. 


## Systems Primary Techs:
In experiment stage, my work is to find out a better pattern to match the user query and subtopic and give their matching probability from the above-mentioned potential features. 
###1.1.	Subtopic Language Model
####1.1.1.	Keyword Extraction
Keywords extraction means to extracted keywords from task statement and subtopic statement. These keywords would be combined as queries to search on Google and crawl their Top 20 result as the language model for subtopic. 
In this study, I use the Stanford Nature language parser package to extraction noun word leaves from statement as a keyword, moreover, if a certain noun word leaf has parent node which is noun phrase, the noun phrase should be used as keyword.

Keyword Extraction Example from Task 2 and Task 5
(Word with “+” means the proper noun)

Keywords for Task 2: 
Emergence, spread, social networking sites, Facebook+, Myspace+, Twitter+, Delicious+, statistics, popularity, sites, users, time, impacts, students, professionals, commerce, money, examples, usage, services, domains, healthcare, politics

Keywords for Task 5:
 friend, four-day vacation, December+, Helsinki+, Finland+, information, vacation, flights, US+, hotels, activities, goal, joint plan, things, Euros, person, group, vacation, outdoor activity, dining activity, cultural activity, types, addition

####1.1.2.	Corpus Set Up
According to the keywords extracted from task or subtopic statement, these keywords can be combined as query for each subtopic. Here are the combination rules: 

 - 1)	Keywords from task statement should regarded as a collection. 
 - 2)	Proper Noun words should regarded as a collection.
 - 3)	Keywords from each subtopic statement should regarded as a collection.
 - 4)	Those keywords come from three collections combined as a query represent for each subtopic.

Using these query and search the TOP 20 results on Google, the titles and snippets on the Google result pages can regarded as the corpus for describing each subtopic .

####1.1.3.	Dirichlet Prior Smoothing
Once the corpus built up, We nned build language model for getting the likehood of each query belong to what kind of subtopic. I use Dirichlet Prior Smoothing (DPS) method to get the probability of each term from a query in a certain subtopic language model. Then get product by these probability. The probability can be regarded as score one.

####1.1.4.	Collection Expansion
However, sometimes the term may not get any frequency in collection which is shown as c(w,D) in equation due to the collection may not rich enough for some terms. To deal with this problem, I tried to use the title of task as query to search on Google and get its TOP 50 result as the collection background.

###1.2.	Query Expansion
####1.2.1.	Expansion Content
For each user query, I first search the query on Google with fetching its TOP 5 results. The titles and snippets are combined as the query’s expansion
####1.2.2.	TFIDF Similarity by VSM
For each query expansion content and subtopic description content (Language model), they can be figured out with two vectors according the terms TFIDF values in two contents. Using the VSM model with these two vectors, then can calculate the similarity between query expansion content and subtopic description. The similarity value can be regarded as score two in this study
###1.3.	Chat Tag
The chat tag is manual annotation for tagging a certain chat record implying a subtopic and the user who send this chat content is under a certain subtopic. With such information, we can general the mapping with user and their responsive subtopic.
###1.4.	Performance Progress
The experiments are stepping by several stages. At the first stage, only language model with Dirichlet Prior Smoothing score applied to evaluate whether a query matching with the subtopic. Under this case, the precision  of experiment is just over than 0.64. Then after inserting the collection background, the precision is improved to 0.73. Finally, with combining DFS score and VSM similarity score, the precision has lift to over 0.81, which is acceptable for the study. So far, we decide to leave the rest of possible features and only use the DFS score and VSM similarity score for the query – subtopic rank system.

##2. System Design:
###2.1 Procedure of System: 
Two main parts make up the entire system: the data preprocess and index retrieval. 

For the data preprocess, in the first step, the user input the task statement with its subtopics statement and then these information should be saved into database. 
The target of the second step is trying to set up the expanded subtopic description that we can regard it as the language model of each subtopic under a certain task. During this part, the data is pretty large and it need to crawl on Google search result, which is very time-consuming. So I separated it with the retrieval part. 

For the index retrieval part, which begin with each time the user login system and select a task. The subtopic data will automatically load into index. When user has a query, the query will search TOP 5 result on Google and set into index. During retrieval, the Dirichlet Prior Smoothing score and TFIDF VSM score should come out as two maps with subtopic ID and corresponding values. Then according to these two values, the final score should as a probability with normalizing the sum of two previous scores.

The following diagram demonstrated the whole procedure of this system. 
![alt tag] (https://github.com/xmruibi/infsci-2950-query-subtopic-probability/blob/master/QTC-Processdure.png)


###2.2.	Classes diagram
The following is the brief diagram introducing the main purpose of the majority of classes in this system without very detailed methods illustration. The note in code can well explain the methods and detail function. 

![alt tag](https://github.com/xmruibi/infsci-2950-query-subtopic-probability/blob/master/Untitled%20Diagram.png)
