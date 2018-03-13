<html>



<!-- Add jquery to local public folder if unable to access public url above -->
<!--  <link rel="stylesheet" href="js/jquery/jquery.mobile-1.4.5.min.css" />
      <script src="js/jquery/jquery-1.11.1.min.js"></script>
      <script src="js/jquery/jquery.mobile-1.4.5.min.js"></script>
-->


   <!-- <script type="text/javascript" src="https://code.jquery.com/jquery-1.10.2.min.js"></script>-->


    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,700" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=PT+Serif:400,700,400italic" rel="stylesheet" type="text/css">
    <link href="https://netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.css" rel="stylesheet" type="text/css">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet" type="text/css">


    <link type="text/css" rel="stylesheet" href="js/metricsjs/metricsgraphics.css"/>
    <link type="text/css" rel="stylesheet" href="js/metricsjs/metricsgraphics-demo-dark.css"/>

    <link type="text/css" rel="stylesheet" href="js/metricsjs/highlightjs-default.css"/>
    <link type="text/css" rel="stylesheet" href="js/metricsjs/railscasts.css"/>

    <!--<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.16/datatables.min.css"/>-->

    <!--<link type="text/css" rel="stylesheet" href="js/metricsjs/metricsgraphics.css"/>-->


    <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
    <!--   <script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.5/jquery.min.js" ></script>
 -->
    <script src="js/d3/d3.v4.min.js"></script>

    <script type="text/javascript" src="js/metricsjs/metricsgraphics.js"></script>
    <!-- <script type="text/javascript" src="js/metricsjs/metricsgraphics.min.js"></script>-->


    <!--
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>

    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
-->
    <link href='' rel='stylesheet' type='text/css' id='dark'>
    <link href='' rel='stylesheet' type='text/css' id='dark-code'>

    <!--<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />-->

    <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>


    <script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.16/datatables.min.js"></script>



<!--
<script language="javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>

    <link type="text/css" rel="stylesheet" href="js/metricsjs/bootstrap-datetimepicker.css"/>

<script type="text/javascript" src="js/metricsjs/moment.js"></script>

<script type="text/javascript" src="js/metricsjs/bootstrap-datetimepicker.min.js"></script>

-->
    <!-- 1024×768 -->
    <style>
        .borderless tr td {
            border: none !important;
            padding-left: 5px !important;
            padding-right: 0px !important;
            padding-top: 0px !important;
            padding-bottom: 0px !important;

        }


        .secontainer{
            width:924px;
            position: absolute;
            left:50%;
            margin:0 0 0 -462px;

        }
        .table{
            padding: 50px !important;
        }
        .setitle{
            vertical-align:middle;
            font-size:xx-large;
            font-weight: lighter;

        }
        .sedselogo{
            width:300px;
        }

        .semetricsdiv{
            padding:25px;
        }
        .setitlediv{
            padding-bottom:25px;

        }
        .setitle{
            width:300px;
        }
        .setitleOptions{
            padding-bottom:25px;
            width:324px;
            text-align:right;
            vertical-align:bottom;
            padding-bottom:0px;
        }
        .setitleOptionsImage{
            width:45px;

            padding-right:20px;
        }
        .setoptagstable{
            font-size:medium;
            font-weight: 400;
            background-color:#272727;
            width:200px
        }

    </style>
    <script type="text/javascript">



                    /* Initialize Button Events and map to action*/
     $(document).ready(function(){


         pollUniqueUsers()
         pollUniqueUsersRollup()
         getTopTweetsRollup()


         var tagstable = $('#toptagstable').DataTable({
             "searching":false,
             "lengthChange":false,
             "pageLength":30,
             "scrollY":        "200px",
             "scrollCollapse": true,
             "paging":         false,
             "info":false,


             "columns": [
                 {  title: 'Hashtag', name: 'Hashtag', data: 'Hashtag', type:'numeric',
                     fnCreatedCell: function (nTd, sData, oData, iRow, iCol) {
                         $(nTd).html("<a href='https://twitter.com/search?q=%23"+oData.Hashtag+"' target='_blank'>"+oData.Hashtag+"</a>");
                     }},
                 {  title: 'Occurrences', name: 'Occurrences', data: 'Occurrences'}
             ],


             "order": [[ 1, "desc" ]]

//style="background-color:#272727"

         })

          tagstable.order( [ 1, 'desc' ] ).draw()



         var hiddendate = $('#hiddendate');
         if (hiddendate.length > 0) {
             $("#hiddendate").datetimepicker({

             });
         }




     });
     function pollUniqueUsers(start) {

         setTimeout(function () {

             $.ajax({
                 url: "getUniqueUsersForToday",
                 type: "get",
                 contentType: "application/json",
                 dataType: "json"
             }).success(function (dto) {


                 if(dto.approximates) {
                     if (dto.approximates != null) {
                         if (dto.approximates.length > 0) {
                             refreshUniqueUserTable(dto.approximates);
                         }
                     }else{
                         console.log("getUniqueUsersForToday:" + JSON.stringify(dto))
                     }
                 }else{
                     console.log("getUniqueUsersForToday:" + JSON.stringify(dto))
                 }

                 $("#uniqueUserQueryTime").text("Response Time: "+dto.resultTime + " ms");

             }).complete(function (dto) {
                 // Recursive Poll
                 pollUniqueUsers();// Call poll after complete
             });
         }, 5000);//two seconds
     }
    function refreshUniqueUserTable(data) {

        //data = [{'time':new Date(), 'uniqueUsers':10}] yyyy-mm-dd HH:mm:ss
        data = MG.convert.date(data, 'time', '%Y-%m-%d %H:%M:%S');
        //console.log("Result=" + JSON.stringify(data));

        MG.data_graphic({
            title: "Unique Users",
            description: "Unique users who have tweeted",
            data: data,
            width:850,
            height: 200,
            buffer:15,
            top:50,
            left:100,
            target: '#mygraph',
            x_accessor: 'time',
            y_accessor: 'uniqueUsers',
            x_label: 'Time',
            y_label: 'Unique Users'


        })


    }
                    function pollUniqueUsersRollup(start) {

                        setTimeout(function () {

                            $.ajax({
                                url: "getUniqueUsersRollup",
                                type: "get",
                                contentType: "application/json",
                                dataType: "json"
                            }).success(function (dto) {


                                // Refresh UI Data
                                if(dto.approximates) {
                                    if (dto.approximates != null) {
                                        if (dto.approximates.length > 0) {
                                            refreshUniqueUserTableRollup(dto.approximates);
                                        }
                                    }else{
                                        console.log("getUniqueUsersRollup:" + JSON.stringify(dto))
                                    }
                                }else{
                                    console.log("getUniqueUsersRollup:" + JSON.stringify(dto))
                                }


                                $("#uniqueUserRollupQueryTime").text("Response Time: "+dto.resultTime + " ms");


                                // refreshAccountTable(dto);
                                // refreshDetailsTable(dto);
                            }).complete(function (dto) {
                                // Recursive Poll
                                pollUniqueUsersRollup();// Call poll after complete
                            });
                        }, 5000);//ten seconds
                    }
                    function refreshUniqueUserTableRollup(data) {

                        //data = [{'time':new Date(), 'uniqueUsers':10}]
                        data = MG.convert.date(data, 'time', '%Y-%m-%d %H:%M:%S');
                        //console.log("Result=" + JSON.stringify(data));

                        MG.data_graphic({
                            title: "Unique Users 10min Aggregate",
                            description: "Unique users who have tweeted",
                            data: data,
                            width: 500,
                            height: 200,
                            buffer:15,
                            top:50,
                            left:100,
                            target: '#mygraphrollup',
                            x_accessor: 'time',
                            y_accessor: 'uniqueUsers',
                            x_label: 'Time',
                            y_label: 'Unique Users'

                        })



                    }
                    function getTopTweetsRollup(start) {

                        setTimeout(function () {

                            $.ajax({
                                url: "getTopTweetsRollup",
                                type: "get",
                                contentType: "application/json",
                                dataType: "json"
                            }).success(function (dto) {




                                //console.log("Result=" + JSON.stringify(dto));

                                if(dto.topTags) {
                                    if (dto.topTags != null) {
                                        formatTableResult(dto.topTags);

                                    }else{
                                        console.log("getTopTweetsRollup:" + JSON.stringify(dto))
                                    }
                                }else{
                                    console.log("getTopTweetsRollup:" + JSON.stringify(dto))
                                }
                                $("#topTweetsQueryTime").text("Response Time: "+dto.resultTime + " ms");

                                // Refresh UI Data


                                // refreshAccountTable(dto);
                                // refreshDetailsTable(dto);
                            }).complete(function (dto) {
                                // Recursive Poll
                                getTopTweetsRollup();// Call poll after complete
                            });
                        }, 15000);//fifteen seconds
                    }
                    function formatTableResult(topTags){

                        //$("#toptagsdiv").empty();

                       // $('#toptagsdiv').append("<table id=\"toptagstable\"></table>" );

                        var tagstable = $('#toptagstable').DataTable()

                        tagstable.rows().remove().draw();

                        $.each(topTags, function(key, value) {
                            tagstable.row.add(
                                    { "Hashtag":key, "Occurrences":value}
                            ).draw(false)
                            tagstable.order( [1, 'desc'] ).draw()
                        });

                        tagstable.columns( 'Occurrences' )
                                .order( 'desc' )
                                .draw();


                    }







    </script>
</head>
<body>
    <div class="secontainer">
        <div class="setitlediv">
            <table >
                <tr><td><img class="sedselogo" src="images/datastaxinvert.png"></td><td class="setitle">Trending Now</td>
                    <td class="setitleOptions">  <!--<input type="text" id="hiddendate" name="hiddendate"/><img id="optionsimage" class="setitleOptionsImage" src="images/OptionsIcon.png" alt="Click for Date Options"></td></tr>-->
                        <!--<div class="container">
                            <div class="row">
                                <div class='col-sm-6'>
                                    <div class="form-group">
                                        <div class='input-group date' id='datetimepicker1'>
                                            <input type='text' class="form-control" />
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                                        </div>
                                    </div>
                                </div>
                                <script type="text/javascript">
                                    $(function () {
                                        $('#datetimepicker1').datetimepicker();
                                    });
                                </script>
                            </div>
                        </div>-->
                </td>
                </tr>
            </table>
        </div>

        <div class="panel-group">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title" style="background-color:#f1f1f1">
                        <a data-toggle="collapse" href="#collapse1">Perspective: 10 Second Window</a>
                    </h4>
                </div>
                <div id="collapse1" class="panel-collapse collapse" style="background-color:#272727" >
                        <div id="mygraph">

                        </div>
                        <div class="panel-footer"><label id="uniqueUserQueryTime"></label></div>
                </div>
            </div>
        </div>

        <div class="panel-group">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title" style="background-color:#f1f1f1">
                        <a data-toggle="collapse" href="#collapse2">Perspective: 10 Minute Window</a>
                    </h4>
                </div>
                <div id="collapse2" class="panel-collapse collapse">
                    <div class="panel-body" style="background-color:#272727">
                        <table>
                            <tr>
                                <td>
                                    <div id="mygraphrollup"/></td>
                                <td>
                                    <table id="toptagstable" class="table table-sm table-hover borderless setoptagstable">
                                    </table>
                                </td>

                            </tr>

                        </table>

                    </div>
                    <div class="panel-footer">
                        <table>
                            <tr>
                                <td style="width:500px"><label id="uniqueUserRollupQueryTime"></label></td>
                                <td><label id="topTweetsQueryTime"></label></td>
                            </tr>
                        </table>
                     </div>
                </div>
            </div>
        </div>


        <div class="panel-group">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title" style="background-color:#f1f1f1">
                        <a data-toggle="collapse" href="#collapse3">Architecture Diagram</a>
                    </h4>
                </div>
                <div id="collapse3" class="panel-collapse collapse" style="background-color:#272727" >
                    <table>
                        <tr>
                            <td><img style="padding-left:3px;padding-right:3px;width:918px" src="images/ApproximationsArchitecture.png"></td>
                        </tr>
                    </table>

                </div>
            </div>
        </div>
        <br/>
        <br/>



    </div>



</body>
</html>