<style>
    .mycarehub-content{
        display: none;
    }

    .pagination {
        font-weight: bold;
    }
</style>
<%
    ui.includeJavascript("mycarehub", "mycarehub.js")
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])

    def sideMenuItems = [
            [ label: "Appointment Requests", href: ui.pageLink("mycarehub", "mycarehubHome#appointmentRequests") ],
            [ label: "Health Diary Entries", href: ui.pageLink("mycarehub", "mycarehubHome#healthDiary") ],
            [ label: "Red Flags", href: ui.pageLink("mycarehub", "mycarehubHome#redFlags") ],
            [ label: "Screening Tool Red Flags", href: ui.pageLink("mycarehub", "mycarehubHome#screeningTool") ],
    ]

%>

<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "My Care Hub", items: sideMenuItems ]) }
</div>
<div class="ke-page-content">
    <div id="appointmentRequests" class="mycarehub-content" >
        ${ ui.includeFragment("mycarehub", "appointmentRequests") }
    </div>
    <div id="healthDiary" class="mycarehub-content">
        ${ ui.includeFragment("mycarehub", "healthDiary") }
    </div>
    <div id="redFlags" class="mycarehub-content">
        ${ ui.includeFragment("mycarehub", "redFlags") }
    </div>
    <div id="screeningTool" class="mycarehub-content">
        ${ ui.includeFragment("mycarehub", "screeningTool") }
    </div>
</div>

<script type="text/javascript">
    jQuery(function() {
        var selectDisplayFragment = function(){
            if (window.location.href.indexOf("#appointmentRequests") > -1) {
                jQuery('.mycarehub-content').hide();
                jQuery('#appointmentRequests').show();
            } else if(window.location.href.indexOf("#healthDiary") > -1){
                jQuery('.mycarehub-content').hide();
                jQuery('#healthDiary').show();
            } else if(window.location.href.indexOf("#redFlags") > -1){
                jQuery('.mycarehub-content').hide();
                jQuery('#redFlags').show();
            } else if(window.location.href.indexOf("#screeningTool") > -1){
                jQuery('.mycarehub-content').hide();
                jQuery('#screeningTool').show();
            } else {
                jQuery('.mycarehub-content').hide();
                jQuery('#appointmentRequests').show();
            }
        }
        selectDisplayFragment();

        jQuery('input[name="query"]').focus();
        jQuery('.ke-menu-item').click(function(){
            selectDisplayFragment();
        });
    });
</script>