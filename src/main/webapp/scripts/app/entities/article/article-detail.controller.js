'use strict';

angular.module('finderApp')
    .controller('ArticleDetailController', function ($scope, $rootScope, $stateParams, $sce, $http, $state, DataUtils, entity, Article, User, ArticleCategory, Tag, Principal, ArticleFavorite) {
    	 Principal.identity().then(function(account) {
             $scope.isAuthenticated = Principal.isAuthenticated;
         });
    	//异步请求时需要在回调函数中对文章内容html转义
//        $scope.article = entity;
    	Article.get({id : $stateParams.id}, function(result) {
            $scope.article = result;
            //angular对html转义,增加信任$sce.trustAsHtml
            $scope.article.content = $sce.trustAsHtml($scope.article.content);
            //文章作者
            $scope.account = $scope.article.user;
            // 右边栏 热门文章
            $scope.hotArticles = $scope.article.hotArticles;
            // 文章评论列表
            $scope.replies = $scope.article.articleReplies;
            // 文章的收藏数
            $scope.articleFavoriteCount = $scope.article.countArticleSaveAid;
            // 当前登录用户是否收藏过该文章
            $scope.isArticleFavoriteCurrentUser = $scope.article.articleFavoriteCurrentUser;
        }, function(response) {
        	// 可能有文章id不存在或是该文章不允许发布
            if (response.status === 404) {
            	// 转到404错误页面
            	$state.go('404');
            }
        });
        $scope.load = function (id) {
            Article.get({id: id}, function(result) {
                $scope.article = result;
            });
        };
        var unsubscribe = $rootScope.$on('finderApp:articleUpdate', function(event, result) {
            $scope.article = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
        
        $scope.abbreviate = DataUtils.abbreviate;
        
        // 是否显示回到顶部按钮
        $(window).scroll(
		function() {
			$(this).scrollTop() > 400 ? $("#go-top-btn").css(
					"display", "block") : $("#go-top-btn").hide()
		});
        // 点击回到顶部按钮
        $("#go-top-btn").click(function(){
//        	document.body.scrollTop=0;document.documentElement.scrollTop=0;
        	scrollTo();
        });
        // 文章评论
        $scope.articleReply = {content:null};
       
        // 保存评论
        $scope.replySave = function () {
        	$scope.isSaving = true;
        	// 设置文章评论对应的文章ID(文章和评论的关联关系)
            $scope.articleReply.article = {id:$scope.article.id};
            // 目的是把json对象转换成字符串
//            JSON.stringify(data)
            // 用$http.post发请求
            $http.post('api/articleDetailsReplys', JSON.stringify($scope.articleReply)).success(function (response) {
            	// 文章评论列表
                $scope.replies = response;
                // 评论发表按钮为可用状态
                $scope.isSaving = false;
            });
        };
        
        // 回到顶部按钮实现函数
        function scrollTo(name, add, speed) {
    	    if (!speed) speed = 300
    	    if (!name) {
    	        $('html,body').animate({
    	            scrollTop: 0
    	        }, speed)
    	    } else {
    	        if ($(name).length > 0) {
    	            $('html,body').animate({
    	                scrollTop: $(name).offset().top + (add || 0)
    	            }, speed)
    	        }
    	    }
    	}
        
        // 文章收藏
        $scope.addFavorite = function (fid) {
        	
        	$scope.articleFavorite = {};
            // 设定所属的文章ID
            $scope.articleFavorite.article = {id:$scope.article.id};
            // $scope.articleFavorite.id = fid
            if (fid != null) {
            	// 取消收藏功能暂不提供,页面不传参数fid的值
            	ArticleFavorite.delete({id: fid}, onAddSuccess);
            } else {
            	// 用户是否登录
            	if ($scope.isAuthenticated()) {
            		// 设定文章收藏不能点击，避免重复提交
            		$scope.isArticleFavoriteCurrentUser = true;
            		
            		ArticleFavorite.save($scope.articleFavorite, onAddSuccess, onAddError);
            	} else {
            		// 转到登录页面
                	$state.go('login');
            	}
            }
        };
        // 文章收藏成功
        var onAddSuccess = function (result) {
        	$scope.articleFavorite = result;
        	// 文章的收藏数
            $scope.articleFavoriteCount = result.countArticleSaveAid;
        	// 已收藏
        	$scope.isArticleFavoriteCurrentUser = true;
        };
        // 文章收藏错误
        var onAddError = function (result) {
        	
        };
        
        /*var onSaveSuccess = function (result) {
        	// $emit — 将事件向上传播到所有子作用域，包括自己。
            $scope.$emit('评论保存成功！', result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };*/
    });
