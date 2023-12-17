// Đảm bảo rằng bạn đã bao quanh mã của mình trong một hàm hoặc blok {} để tránh xung đột biến toàn cục
(function () {
    // Khai báo ứng dụng AngularJS
    var app = angular.module("myapp", ['ngSanitize']);
    app.controller("myctrl", function ($scope, $http) {

        var selectedTende = localStorage.getItem('selectetende');



        $scope.currentPage = selectedTende;




        if (usersData) {
            $scope.users = usersData;
            // Các đoạn mã khác của bạn
        } else {
            console.error("usersData is not defined or null");
        }



        // Khởi tạo các biến và hàm điều khiển khác nếu cần
        $scope.showExplanation = false;
        $scope.showExplanationnd = false;
        $scope.submitButtonVisible = false;

        angular.forEach($scope.users, function (sv, index) {
            sv.questionNumber = index + 1; // Số thứ tự bắt đầu từ 1
        });
        /* console.log(questionNumber)*/


        $scope.check = function () {
            var diem = 0;
            angular.forEach($scope.users, function (sv) {
                if (sv.selectedAnswer === sv.kq) {
                    diem++;
                } else {
                    sv.isWrong = true;
                }
                angular.forEach($scope.users, function (sv) {
                    angular.forEach(sv.Answers, function (ans) {
                        if (ans.value === sv.kq) {
                            ans.isCorrect = true;
                        }
                    });
                });
                $scope.showExplanation = true;
                $scope.showExplanationnd = true;
                sv.isUserAnswer = sv.selectedAnswer;
                $scope.submitButtonVisible = true;
            });


            var diemString = diem.toString();

            var tenBoDe = localStorage.getItem('selecteten');

            // Gửi điểm và các dữ liệu khác lên server Java
            $http.post('/saveTongket', { diem: diemString, tenBoDe: tenBoDe })
                .then(function (response) {
                    // Xử lý kết quả nếu cần
                    var generatedId = response.data;
                    localStorage.setItem('tongketId', generatedId);
                    console.log("id" + tongketId);
                })
                .catch(function (error) {
                    console.error('Error saving tongket:', error);
                });




            alert("You got " + diemString + " marks");

        };

        $scope.exit = function () {
            const localhostAddress = "http://localhost:8080/index"; // Thay thế 3000 bằng số cổng bạn đang sử dụng
            window.location.href = localhostAddress;
        }





        $scope.captureScreenshot = async function () {
            const canvas = await html2canvas(document.getElementById('quiz-form'));
            const imageData = canvas.toDataURL();

            try {
                const tongketId = window.localStorage.getItem('tongketId') || '';
                console.log("id: " + tongketId);

                const response = await $http.post('/api/screenshots', { imageData, tongketId }, {
                    headers: { 'Content-Type': 'application/json' }
                });

                console.log("anh: " + imageData);

                const imagePath = response.data;
                if (response.status === 200) {
                    displayLink(imagePath);
                } else {
                    console.error('Failed to upload screenshot');
                }
            } catch (error) {
                console.error('Error uploading screenshot:', error);
            }
        };

        function displayLink(imagePath) {
            $scope.imagePath = imagePath;
        }




/*var easyQuestions = usersData.filter(function(question) {
return question.cb === 2;
});

var hardQuestions = usersData.filter(function(question) {
return question.cb === 1;
});

// Lấy ngẫu nhiên 1 câu hỏi dễ và 4 câu hỏi khó
var randomEasyQuestion = easyQuestions[Math.floor(Math.random() * easyQuestions.length)];
var randomHardQuestions = [];
while (randomHardQuestions.length < 10) { var randomHardQuestion=hardQuestions[Math.floor(Math.random() *
    hardQuestions.length)]; if (!randomHardQuestions.includes(randomHardQuestion)) {
    randomHardQuestions.push(randomHardQuestion); } } // Gộp câu hỏi dễ và câu hỏi khó để hiển thị trên trang web
    $scope.filteredQuestions=[randomEasyQuestion, ...randomHardQuestions];*/ });
})();