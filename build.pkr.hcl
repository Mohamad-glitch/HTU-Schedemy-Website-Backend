packer {
  required_plugins {
    amazon = { version = ">= 1.2.8", source = "github.com/hashicorp/amazon" }
    ansible = { version = ">= 1.1.0", source = "github.com/hashicorp/ansible" }
  }
}

source "amazon-ebs" "java_app" {
  ami_name      = "java-app-{{timestamp}}"
  instance_type = "t3.micro"
  region        = "eu-north-1"
  ssh_username  = "ubuntu"

  source_ami_filter {
    filters = {
      name                = "ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"
      root-device-type    = "ebs"
      virtualization-type = "hvm"
    }
    most_recent = true
    owners      = ["099720109477"]
  }
}

build {
  sources = ["source.amazon-ebs.java_app"]
  provisioner "ansible" {
    playbook_file = "./deploy.yml"
    user          = "ubuntu"
    ansible_env_vars = ["ANSIBLE_PYTHON_INTERPRETER=/usr/bin/python3"]
  }
}
