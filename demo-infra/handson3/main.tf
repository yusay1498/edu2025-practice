resource "local_file" "foo" {
  filename = "foo.txt"
  content  = "bar-${var.environment}"
}