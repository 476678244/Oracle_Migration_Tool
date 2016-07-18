mongod --dbpath /local/data/db --logpath /local/data/db/mongo.log --fork --port 27017
cd /local/tomcat/apache-tomcat-8.0.36/bin
./shutdown.sh
cd ../webapps
rm -rf spring-based
cd /local/git/spring-based
git pull origin data_manage
mvn install
cp -r target/springbased-1.0 ../../tomcat/apache-tomcat-8.0.36/webapps/
cd /local/tomcat/apache-tomcat-8.0.36/bin
./startup.sh
vi test
