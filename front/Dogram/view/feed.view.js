class FeedView {
  constructor() {}
  feedConstructor() {
    this.feedSearchBtn = document.querySelector(".feed_searchBtn");
    this.container = document.querySelector(".content");
    this.feedItem = document.querySelector(".feed_item");
    this.likeBtn = document.querySelector(".likebtn");
    this.likeBtnFar = document.querySelector(".likebtn > i");
    console.log(this.container);
  }
  bindAddFeed(containerLoad) {
    window.addEventListener("scroll", containerLoad, { passive: true });
  }
  bindAddLike(addLike) {
    this.likeBtn.addEventListener("click", addLike);
    console.log(this.likeBtn);
  }
}
export default FeedView;
