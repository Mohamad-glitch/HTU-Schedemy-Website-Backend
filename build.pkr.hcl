packer {
  required_plugins {
    amazon = {
      version = ">= 1.2.8"
      source  = "github.com/hashicorp/amazon"
    }
    ansible = {
      version = ">= 1.1.0"
      source  = "github.com/hashicorp/ansible"
    }
  }
}

source "amazon-ebs" "java_app" {
  ami_name      = "java-app-{{timestamp}}"
  instance_type = "t3.micro"
  region        = "eu-north-1"
  ssh_username  = "ec2-user" # Amazon Linux uses ec2-user

  source_ami_filter {
    filters = {
      # This pattern finds the latest Amazon Linux 2023
      name                = "al2023-ami-2023.*-x86_64"
      root-device-type    = "ebs"
      virtualization-type = "hvm"
    }
    most_recent = true
    owners      = ["137112412989"] # Official Amazon Owner ID
  }
}

build {
  sources = ["source.amazon-ebs.java_app"]

  provisioner "ansible" {
    playbook_file = "./deploy.yml"
    user          = "ec2-user" # Must match ssh_username
    use_proxy     = false
    ansible_env_vars = [
      "ANSIBLE_HOST_KEY_CHECKING=False",
      "ANSIBLE_PYTHON_INTERPRETER=/usr/bin/python3"
    ]
  }
}
