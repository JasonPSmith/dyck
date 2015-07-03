<html>
<body>
<form action="dyck.php" method="GET"><br><br>
  <font color=blue>Enter two Dyck paths, in the form 0101 for UDUD, for example</font><br><br>
  Bottom path:&nbsp;&nbsp; <input type="text" name="num0">&nbsp;&nbsp;
  Top path:&nbsp;&nbsp;<input type="text" name="num1"><p>
    <input type="submit">
</form>		   
<?php
   if(isset($_REQUEST["num0"])) {$num0 = $_REQUEST{"num0"}; $inp[0] = $_REQUEST{"num0"};}
   if(isset($_REQUEST["num1"])) {$num1 = $_REQUEST{"num1"}; $inp[1] = $_REQUEST{"num1"};}

   if(isset($_REQUEST["num0"]))
   echo `java dyckInterval $inp[0] $inp[1]`;
?>
</html>