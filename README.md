# Oracle Migration Tool : Data Manage 
mvn install:install-file -Dfile=ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0.4.0 -Dpackaging=jar

1.add ojdbc14.jar to tomcat/lib

2.add content to tomcat-users.xml (tomcat)
    <role rolename="manager"/>
    <role rolename="admin"/>
    <role rolename="manager-gui"/>
    <role rolename="manager-script"/>
    <user username="admin" password="admin123" roles="manager-script,manager-gui,manager,admin"/>
	</tomcat-users>

3.add content to settings.xml (maven)
	<server>
		<id>tomcat8x</id>
		<username>admin</username>
		<password>admin123</password>
	</server>
  	</servers>

mvn tomcat7:redeploy

open questions :

 select distinct USERS_GROUP_ID from ( select * from SFUSER_TREE.USRGRP_MAP order by USERS_group_ID );

 select distinct USERS_GROUP_ID from ( select * from SFUSER_TREE.USRGRP_MAP order by USERS_group_ID ) where ROWNUM < 50 ;

 select distinct USERS_GROUP_ID from (  select * from SFUSER_TREE.USRGRP_MAP order by USERS_sys_ID desc  ) where ROWNUM < 50 ;

