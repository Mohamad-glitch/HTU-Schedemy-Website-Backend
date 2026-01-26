packer {
  required_plugins {
    amazon = { version = ">= 1.2.8", source = "github.com/hashicorp/amazon" }
    ansible = { version = ">= 1.1.0", source = "github.com/hashicorp/ansible" }
  }
}

source "amazon-ebs" "java_app" {
  ami_name      = "java-app-{{timestamp}}"
  instance_type = "t3.micro"
  region        = "us-east-1"
  source_ami    = "ami-0c7217cdde317cfec" # Base Ubuntu AMI
  ssh_username  = "ubuntu"
}

build {
  sources = ["source.amazon-ebs.java_app"]
  provisioner "ansible" {
    playbook_file = "./deploy.yml"
  }
}
