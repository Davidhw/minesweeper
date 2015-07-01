Features: The game works. It has a play again button. 

Design: The primary intellegence is in board. GridLocation and Tiles don't know about Board. GridLocation just replaces tuples, and Tiles just store the values Board gives them. The only intellegence Tiles have is in determining what their label should be (" " if unvisited, X for bomb, number of adjacent bombs if any, or " ".) That every function that could change Tile's label has to call updateLabel is probably bad design, but it was done rather than the normal getter pattern because you can only serialize attributes, not getters.

Main sets up "/play" (to begin) and "/move" (to post moves to). move.ft exists to outut the board state so that the success function from a post to move can easily get the new board. 

main.ftl's main purpose is embed the canvas, new game button, and board in the html. main.js handles user clicks and displaying the board. main.js actually is the only place where error checking of user input is done. This somewhat violates MVC, but since the ways in which the user can provide input are so limited, it makes sense to me to just quickly ignore clicks that occur outside the canvas rather than letting those clicks get posted to the server.

Known Bugs:
This isn't a bug se, but when displaying the tiles labels I have to increment height (j) by one. "ctx.fillText(board.grid[i][j].label,i*vcW,(j+1)*vcH);". I'm not sure why I have to increment height (j) by one, but not width (i). Note that vcW and vcH are both 20 in the 16 x 16 squares on a 320 x 320 canvas case.

CheckStyle Errors: I have 143 errors left. I don't think all of them are things that matter. Trailing whitespace aren't bad as they're invisible and don't affect the program. I also don't see the need to declare passed in arguments or methods as final as I don't mind if someone who needs to change them changes them in the future. 
