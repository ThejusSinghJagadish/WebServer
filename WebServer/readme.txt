To compile and run the Server.java, execute the following server.sh

./server.sh -document_root "/Users/thejussinghj/Documents/workspace/WebServer/" -port 9199

Note: Please make sure you provide the correct document root inorder to access the files in that root.


On the Client Side(Web Browser)

1. enter localhost:<port> in address bar
2. by default the output would be  www.scu.edu homepage
3. requesting /index.html would also output www.scu.edu homepage
4. requesting anyother file, would display the file if present in the specified document root else will display a 404 Error.

Screen shots are present in the Screen Shot folder.