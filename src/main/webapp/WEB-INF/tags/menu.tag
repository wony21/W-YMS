<%@ tag language="java" pageEncoding="EUC-KR"%>

<div class="ui secondary  menu">
  <a id="a-menu" name="mn-file" class="item" href="/ifcheck">
	설비수율
  </a>
  <a id="a-menu" name="mn-usage" class="item" href="/usage">
	사용량
  </a>
  <a id="a-menu" name="mn-operation" class="item" href="/operation">
	미일치공정
  </a>
  <a id="a-menu" name="mn-calendar" class="item" href="/calendar">
	달력
  </a>
  <a id="a-menu" name="mn-ctq" class="item" href="/ctq">
	CTQ
  </a>
  <a id="a-menu" name="mn-popif" class="item" href="/popIF">
	I/F
  </a>
  <div class="right menu">
    <!-- <div class="item">
      <div class="ui icon input">
        <input type="text" placeholder="Search...">
        <i class="search link icon"></i>
      </div>
    </div>
    <a class="ui item">
      Logout
    </a> -->
  </div>
</div>

<script>

$(function(){

	$(document).ready(function(e){
		console.log('ready.');
		console.log(window.location.href);
		console.log(window.location.pathname);
		var path = window.location.pathname;
		if ( path == '/ifcheck') {
			$("[name^=mn-file]").attr('class', 'item active');
		} else if ( path == '/usage' ) {
			$("[name^=mn-usage]").attr('class', 'item active');
		} else if ( path == '/operation' ) {
			$("[name^=mn-operation]").attr('class', 'item active');
		} else if ( path == '/calendar' ) {
			$("[name^=mn-calendar]").attr('class', 'item active');
		} else if ( path == '/ctq' ) {
			$("[name^=mn-ctq]").attr('class', 'item active');
		} else if ( path == '/popIF' ) {
			$("[name^=mn-popif]").attr('class', 'item active');
		}
	});
	
	/* $(document.body).on('click', 'a[id="a-menu"]', function(event){
		$("[id^=a-menu]").each(function() {
			console.log($(this));
			$(this).attr('class', 'item');
		});
		$(this).attr('class','item active');
	}); */
});

</script>