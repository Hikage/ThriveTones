C = /usr/local/jdk-1.5-0-02-nb-4/jdk1.5/bin/javac 
E = /usr/local/jdk-1.5-0-02-nb-4/jdk1.5/bin/java

Z = Maze_Project.zip

Dir0 =  .
Dir1 =  Driver
Dir2 =  $(Dir1)/SimpleTestData
Dir3 =  $(Dir1)/RealTestData
Dir4 =  $(Dir0)/Fog_of_War_example
Dir5 =  $(Dir0)/MapDisp
Dir6 =  $(Dir0)
Dir7 =  $(Dir0)/Player
Dir8 =  $(Dir0)/Nagotiator_Distracter
Dir9 =  $(Dir0)/Items
Dir10 = $(Dir0)
Dir11 = $(Dir0)/GUI
Dir12 = $(Dir0)
Dir13 = $(Dir0)/Maze
Dir14 = $(Dir13)/Barrier


$(Dir1)/MazeRunner.class: $(Dependancies2)
	$C $(Dir1)/MazeRunner.java

run:
	$E $(Dir1)/MazeRunner



clean:	
	rm -f $(Dir0)/*.class
	rm -f $(Dir0)/*~
	rm -f $(Dir1)/*.class
	rm -f $(Dir1)/*~
	rm -f $(Dir2)/*.class
	rm -f $(Dir2)/*~
	rm -f $(Dir3)/*.class
	rm -f $(Dir3)/*~
	rm -f $(Dir4)/*.class
	rm -f $(Dir4)/*~
	rm -f $(Dir5)/*.class
	rm -f $(Dir5)/*~
	rm -f $(Dir6)/*.class
	rm -f $(Dir6)/*~
	rm -f $(Dir7)/*.class
	rm -f $(Dir7)/*~
	rm -f $(Dir8)/*.class
	rm -f $(Dir8)/*~
	rm -f $(Dir9)/*.class
	rm -f $(Dir9)/*~
	rm -f $(Dir10)/*.class
	rm -f $(Dir10)/*~	
	rm -f $(Dir11)/*.class
	rm -f $(Dir11)/*~		
	rm -f $(Dir12)/*.class
	rm -f $(Dir12)/*~
	rm -f $(Dir13)/*.class
	rm -f $(Dir13)/*~
	rm -f $(Dir14)/*.class
	rm -f $(Dir14)/*~

	touch $(Dir1)/MazeRunner.java

zip:
	rm $(Z) -f
	rm -r temp -f
	zip $(Z) $(Dir0)/* $(Dir1)/* $(Dir2)/* $(Dir3)/* $(Dir4)/* $(Dir5)/* $(Dir6)/* $(Dir7)/* $(Dir8)/* $(Dir9)/* $(Dir10)/* $(Dir11)/* $(Dir12)/* $(Dir13)/* $(Dir14)/*
	mkdir temp
	cp $(Z) temp/	
	#unzip temp/$(Z) temp

tar:
	#tar -cvf Maze_Package.tar Maze/* Maze/Barrier/* Maze/Testing/* Maze/Testing/SimpleTestData/* makefile MazeMakeFile
	#bzip2 Maze_Package.tar 

chmod:
	chmod go+r+w+X -R * .project .classpath .settings/ 
	chmod u+r+w+X  -R * .project .classpath .settings/


